package org.fireflyest.pamphlet;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.craftcommand.argument.NumberArgs;
import org.fireflyest.craftdatabase.sql.SQLConnector;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.pamphlet.command.PamphletCommand;
import org.fireflyest.pamphlet.command.PlaytimeCommand;
import org.fireflyest.pamphlet.command.RewardCommand;
import org.fireflyest.pamphlet.command.SignCommand;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.PamphletYaml;
import org.fireflyest.pamphlet.gui.ExchangeView;
import org.fireflyest.pamphlet.gui.ExpView;
import org.fireflyest.pamphlet.gui.ProgressView;
import org.fireflyest.pamphlet.gui.RewardView;
import org.fireflyest.pamphlet.listener.PlayerEventListener;
import org.fireflyest.pamphlet.service.PamphletService;


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
 * 4、钓鱼
 * 5、挖坑
 * 6、击杀
 * 7、副本通关
 * 8、邀请码
 * 9、市场交易
 * 10、获得金币
 * 11、登录3天
 * 12、使用道具
 * 13、抽奖3次
 * 14、完成跑酷
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
    private String url;

    public static final String VIEW_EXP = "pamphlet.exp";
    public static final String VIEW_PROGRESS = "pamphlet.progress";
    public static final String VIEW_EXCHANGE = "pamphlet.exchange";
    public static final String VIEW_REWARD = "pamphlet.reward";
    public static final String VIEW_EDIT = "pamphlet.edit";

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
        this.getServer().getPluginManager().registerEvents(new PlayerEventListener(service), this);
        // this.getServer().getPluginManager().registerEvents(new TaskEventListener(service), this);

        
        // commands
        this.setupCommand();
    }

    @Override
    public void onDisable() {
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
        guide.addView(VIEW_PROGRESS, new ProgressView(service));
        guide.addView(VIEW_EXCHANGE, new ExchangeView(service));
        guide.addView(VIEW_REWARD, new RewardView(service, guide));
    }
    
    private void setupCommand() {
        PluginCommand pamphlet = this.getCommand("pamphlet");
        if (pamphlet != null) {
            PamphletCommand pamphletCommand = new PamphletCommand(guide);
            SignCommand signCommand = new SignCommand();
            PlaytimeCommand playtimeCommand = new PlaytimeCommand();
            RewardCommand rewardCommand = new RewardCommand(guide);
            rewardCommand.setArgument(0, new NumberArgs());
            pamphletCommand.addSubCommand("sign", signCommand);
            pamphletCommand.addSubCommand("playtime", playtimeCommand);
            pamphletCommand.addSubCommand("reward", rewardCommand);
            pamphlet.setExecutor(pamphletCommand);
            pamphlet.setTabCompleter(pamphletCommand);
        }
    }

}
