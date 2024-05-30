package org.fireflyest.pamphlet.gui;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.api.View;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.util.TranslateUtils;
import org.fireflyest.pamphlet.bean.Reward;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.RandomUtils;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.StringUtils;
import org.fireflyest.util.TimeUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RewardView implements View<RewardPage> {

    private static final Pattern varPattern = Pattern.compile("%([^%]*)%");

    private final PamphletService service;
    private final ViewGuide guide;

    private RewardPage rewardPage;

    public RewardView(PamphletService service, ViewGuide guide) {
        this.service = service;
        this.guide = guide;
    }

    @Override
    public RewardPage getFirstPage(@Nullable String target) {
        if (rewardPage == null) {
            // target为周目号
            rewardPage = new RewardPage(target, service, guide);
        }
        return rewardPage;
    }

    @Override
    public void removePage(@Nullable String target) {
        // 
    }

    /**
     * 分发奖励
     * @param player 玩家
     * @param reward 奖励
     */
    public static boolean handOutReward(@Nonnull Player player, @Nullable Reward reward) {
        if (reward == null) {
            player.sendMessage(Language.REWARD_NULL);
            return false;
        }
        String commands = reward.getCommands();
        // 物品奖励或者指令奖励
        if (commands == null) {
            // 获取物品
            ItemStack rewardItem = SerializationUtil.deserializeItemStack(reward.getItem());
            player.getInventory().addItem(rewardItem);
            // 奖励提示
            String rewardItemName = reward.getName();
            if (rewardItemName == null) {
                rewardItemName = ItemUtils.getDisplayName(rewardItem);
            }
            if (rewardItemName == null || "".equals(rewardItemName)) {
                rewardItemName = TranslateUtils.translate(rewardItem.getType());
            }
            player.sendMessage(StringUtils.format(Language.REWARD_ITEM, rewardItemName, rewardType(reward)));
        } else {
            Gson gson = new Gson();
            List<String> commandsList = gson.fromJson(commands, new TypeToken<List<String>>() {}.getType());
            for (String command : commandsList) {
                // 替换参数
                command = varReplace(command, player.getName());
                // 控制台执行指令
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            // 奖励提示
            String rewardItemName = reward.getName();
            player.sendMessage(StringUtils.format(Language.REWARD_COMMANDS, rewardItemName, rewardType(reward)));
        }
        return true;
    }

    /**
     * 获取奖励的获取条件类型
     * @param reward 奖励
     * @return 奖励条件
     */
    private static String rewardType(Reward reward) {
        switch (reward.getType()) {
            case RewardPage.REWARD_SIGN:
                return "每日签到";
            case RewardPage.REWARD_SERIES_SIGN:
                return "连续签到" + reward.getNum() + "次";
            case RewardPage.REWARD_CUMULATIVE_SIGN:
                return "周目累计签到" + reward.getNum() + "次";
            case RewardPage.REWARD_PLAYTIME:
                return "当天在线" + TimeUtils.duration(reward.getNum());
            case RewardPage.REWARD_SEASON_PLAYTIME:
                return "周目累计在线" + TimeUtils.duration(reward.getNum());
            default:
                break;
        }
        return null;
    }

    /**
     * 替换指令里的参数
     * @param command 指令
     * @param playerName 玩家名称
     * @return 参数替换后的指令
     */
    private static String varReplace(@Nonnull String command, @Nonnull String playerName) {
        // 替换变量
        Matcher varMatcher = varPattern.matcher(command);
        StringBuilder stringBuilder = new StringBuilder();
        while (varMatcher.find()) {
            String parameter = varMatcher.group();
            String parameterName = parameter.substring(1, parameter.length() - 1);
            if ("player".equals(parameterName)) { // %player%
                parameterName = playerName;
            } else if (parameterName.contains("random")) { // %random-1-10%
                String[] randomVar = parameterName.split("-");
                int randomInt = RandomUtils.randomInt(NumberConversions.toInt(randomVar[1]), NumberConversions.toInt(randomVar[2]));
                parameterName = String.valueOf(randomInt);
            }
            varMatcher.appendReplacement(stringBuilder, parameterName);
        }
        varMatcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }

}
