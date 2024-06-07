package org.fireflyest.pamphlet;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.craftcommand.argument.NumberArgs;
import org.fireflyest.craftcommand.argument.PlayerArgs;
import org.fireflyest.craftdatabase.sql.SQLConnector;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.TaskHandler;
import org.fireflyest.pamphlet.bean.Diary;
import org.fireflyest.pamphlet.command.PamphletCommand;
import org.fireflyest.pamphlet.command.PlaytimeCommand;
import org.fireflyest.pamphlet.command.RewardCommand;
import org.fireflyest.pamphlet.command.SeasonAlterCommand;
import org.fireflyest.pamphlet.command.SeasonArgument;
import org.fireflyest.pamphlet.command.SeasonCommand;
import org.fireflyest.pamphlet.command.SeasonCreateCommand;
import org.fireflyest.pamphlet.command.SeasonResetCommand;
import org.fireflyest.pamphlet.command.SignCommand;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.PamphletYaml;
import org.fireflyest.pamphlet.gui.EditView;
import org.fireflyest.pamphlet.gui.ExchangeView;
import org.fireflyest.pamphlet.gui.ExpView;
import org.fireflyest.pamphlet.gui.ProgressView;
import org.fireflyest.pamphlet.gui.RewardView;
import org.fireflyest.pamphlet.gui.SeasonView;
import org.fireflyest.pamphlet.listener.PlayerEventListener;
import org.fireflyest.pamphlet.listener.TaskEventListener;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.pamphlet.task.ProgressTaskFactory;
import org.fireflyest.util.TimeUtils;


/*
 * 每日任务 (赛季币换赛季物品)
 * 1、登录
 * 2、
 * 3、进行两次游戏
 * 4、在线20分钟
 * 每周任务 (逐周开放，十二周一个赛季)
 * 1、添加好友
 * 2、拜访地皮
 * 3、参与划船竞速
 * 4、钓鱼 *
 * 5、挖矿 *
 * 6、击杀 *
 * 7、副本通关
 * 8、邀请码
 * 9、市场交易
 * 10、获得金币
 * 11、登录3天
 * 12、使用道具
 * 13、抽奖3次
 * 14、完成跑酷
 * 15、村民交易 *
 * 赛季任务 
 * 1、在线时长
 * 手册 (100手册经验为一级)
 * 1、购买等级-50点券
 * 2、基础班奖励
 * 3、进阶版奖励-300点券
 * 4、兑换商城
 * 
 */
public final class Pamphlet extends JavaPlugin {
    
    private ViewGuide guide;
    private PamphletYaml yaml;
    private PamphletService service;
    private TaskHandler handler;
    private String url;

    public static final String KEY_PLAYER_PLAYTIME = "pamphlet.playtime."; // 玩家在线时间

    public static final String VIEW_EXP = "pamphlet.exp";
    public static final String VIEW_PROGRESS = "pamphlet.progress";
    public static final String VIEW_EXCHANGE = "pamphlet.exchange";
    public static final String VIEW_REWARD = "pamphlet.reward";
    public static final String VIEW_EDIT = "pamphlet.edit";
    public static final String VIEW_SEASON = "pamphlet.season";

    public static final String TASK_PROGRESS = "pamphlet.progress";

    public static Pamphlet getPlugin() {
        return getPlugin(Pamphlet.class);
    }

    @Override
    public void onEnable() {
        // data service
        this.getLogger().info("Enable data service.");
        yaml = new PamphletYaml(this);
        try {
            if (Config.SQL) {
                url = Config.URL;
                SQLConnector.setupConnect(SQLConnector.MYSQL, url, Config.USER, Config.PASSWORD);
            } else {
                url = "jdbc:sqlite:" + getDataFolder().getParent() + "/" + this.getClass().getSimpleName() + "/storage.db";
                SQLConnector.setupConnect(SQLConnector.SQLITE, url, null, null);
            }
            service = new PamphletService(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // gui service
        this.setupGuide();
        
        // listener
        this.getLogger().info("Lunching listener.");
        this.getServer().getPluginManager().registerEvents(new PlayerEventListener(service, guide), this);
        this.getServer().getPluginManager().registerEvents(new TaskEventListener(handler), this);

        
        // commands
        this.setupCommand();

        // 重置在线数据
        for (Player player : Bukkit.getOnlinePlayers()) {
            String diaryTarget = player.getUniqueId().toString() + "-" + TimeUtils.getLocalDate();
            Diary diary = service.selectDiaryByTarget(diaryTarget);
            Validate.notNull(diary, "diaryTarget " + diaryTarget + " is null!");
            // 记录在线时间
            long playtime = diary.getPlaytime();
            guide.getViewVariable().set(Pamphlet.KEY_PLAYER_PLAYTIME + player.getUniqueId().toString(), String.valueOf(playtime));
        }
    }

    @Override
    public void onDisable() {
        // 记录在线时间
        for (Player player : Bukkit.getOnlinePlayers()) {
            // 获取连续在线时间并入当日在线时间
            String playtimeKey = Pamphlet.KEY_PLAYER_PLAYTIME + player.getUniqueId().toString();
            String diaryTarget = player.getUniqueId().toString() + "-" + TimeUtils.getLocalDate();
            service.updateDiaryPlaytimeAdd(diaryTarget, guide.getViewVariable().age(playtimeKey) * 1000);
            // 清除连续在线
            guide.getViewVariable().del(playtimeKey);
        }
        // close data service
        if (service != null) {
            SQLConnector.close(url);
        }
    }

    private void setupGuide() {
        RegisteredServiceProvider<ViewGuide> rsp = Bukkit.getServicesManager().getRegistration(ViewGuide.class);
        if (rsp == null) {
            this.getLogger().warning("ViewGuide not found!");
            return;
        }
        guide = rsp.getProvider();

        guide.addView(VIEW_EXP, new ExpView(service, guide));
        guide.addView(VIEW_PROGRESS, new ProgressView(service, yaml));
        guide.addView(VIEW_EXCHANGE, new ExchangeView(service));
        guide.addView(VIEW_REWARD, new RewardView(service, guide));
        guide.addView(VIEW_EDIT, new EditView(service, guide));
        guide.addView(VIEW_SEASON, new SeasonView(service));

        RegisteredServiceProvider<TaskHandler> hrsp = Bukkit.getServicesManager().getRegistration(TaskHandler.class);
        if (hrsp == null) {
            this.getLogger().warning("TaskHandler not found!");
            return;
        }
        handler = hrsp.getProvider();

        handler.prepareTask(TASK_PROGRESS, new ProgressTaskFactory(service));
    }
    
    private void setupCommand() {
        PluginCommand pamphlet = this.getCommand("pamphlet");
        if (pamphlet != null) {
            PamphletCommand pamphletCommand = new PamphletCommand(service, guide);
            SignCommand signCommand = new SignCommand(service, guide);
            PlaytimeCommand playtimeCommand = new PlaytimeCommand(service, guide);
            RewardCommand rewardCommand = new RewardCommand(guide);
            rewardCommand.setArgument(0, new NumberArgs());
            pamphletCommand.addSubCommand("sign", signCommand);
            pamphletCommand.addSubCommand("playtime", playtimeCommand);
            pamphletCommand.addSubCommand("reward", rewardCommand);
            pamphlet.setExecutor(pamphletCommand);
            pamphlet.setTabCompleter(pamphletCommand);
        }
        PluginCommand season = this.getCommand("season");
        if (season != null) {
            SeasonCommand seasonCommand = new SeasonCommand();
            SeasonAlterCommand seasonAlterCommand = new SeasonAlterCommand(service, yaml);
            seasonAlterCommand.setArgument(0, new SeasonArgument(service));
            SeasonCreateCommand seasonCreateCommand = new SeasonCreateCommand(service);
            SeasonResetCommand seasonResetCommand = new SeasonResetCommand(service, guide);
            seasonResetCommand.setArgument(0, new PlayerArgs());
            seasonCommand.addSubCommand("alter", seasonAlterCommand);
            seasonCommand.addSubCommand("create", seasonCreateCommand);
            seasonCommand.addSubCommand("reset", seasonResetCommand);
            season.setExecutor(seasonCommand);
            season.setTabCompleter(seasonCommand);
        }
    }

}
