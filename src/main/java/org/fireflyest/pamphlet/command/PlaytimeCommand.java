package org.fireflyest.pamphlet.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.pamphlet.Pamphlet;
import org.fireflyest.pamphlet.bean.Diary;
import org.fireflyest.pamphlet.bean.Reward;
import org.fireflyest.pamphlet.bean.Steve;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.gui.RewardPage;
import org.fireflyest.pamphlet.gui.RewardView;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.RandomUtils;
import org.fireflyest.util.StringUtils;
import org.fireflyest.util.TimeUtils;

public class PlaytimeCommand extends SubCommand {

    private final PamphletService service;
    private final ViewGuide guide;

    public PlaytimeCommand(PamphletService service, ViewGuide guide) {
        this.service = service;
        this.guide = guide;
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender) {
        Player player = (sender instanceof Player) ? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.ONLY_PLAYER_USE);
            return false;
        }

        // 是否进行周目更新
        if (service.selectSteveSeasonByUid(player.getUniqueId()) != Config.SEASON) {
            player.sendMessage(Language.SEASON_RESET);
            return true;
        }

        // 今天数据
        String diaryTarget = player.getUniqueId().toString() + "-" + TimeUtils.getLocalDate();
        Diary todayDiary = service.selectDiaryByTarget(diaryTarget);

        // 如果当天没有数据先插入
        if (todayDiary == null) {
            todayDiary = new Diary(diaryTarget, Config.SEASON);
            service.insertDiary(diaryTarget, Config.SEASON);
        }

        String playtimeKey = Pamphlet.KEY_PLAYER_PLAYTIME + player.getUniqueId().toString();
        long todayPlaytime = guide.getViewVariable().age(playtimeKey) * 1000 + NumberConversions.toLong(guide.getViewVariable().get(playtimeKey));
        // 取出玩家可获取的当天在线奖励
        Reward[] diaryRewards = service.selectRewardByType(RewardPage.REWARD_PLAYTIME, Config.SEASON);
        String diaryQuota = this.playtimeReward(player, diaryRewards, todayDiary.getQuota(), todayPlaytime);
        service.updateDiaryPlaytimeQuota(diaryTarget, diaryQuota);
        
        
        long seasonPlaytime = service.selectSeasonPlaytime(player.getUniqueId().toString(), Config.SEASON) + todayPlaytime;
        // 取出玩家可获取的周目在线奖励
        Reward[] seasonRewards = service.selectRewardByType(RewardPage.REWARD_SEASON_PLAYTIME, Config.SEASON);
        Steve steve = service.selectSteveByUid(player.getUniqueId());
        String seasonQuota = this.playtimeReward(player, seasonRewards, steve.getQuota(), seasonPlaytime);
        service.updateSeasonPlaytimeQuota(player.getUniqueId(), seasonQuota);
        
        // 领取后更新当天在线，清除连续在线到每日在线，防止过凌晨重复领取奖励
        service.updateDiaryPlaytimeAdd(diaryTarget, guide.getViewVariable().age(playtimeKey) * 1000);
        guide.getViewVariable().set(playtimeKey, String.valueOf(todayPlaytime));

        return true;
    }

    /**
     * 给在线奖励
     * @param player 玩家
     * @param diaryRewards 奖励集
     * @param quotaString 限额
     * @param playtime 在线时长
     * @return 新限额
     */
    private String playtimeReward(Player player, Reward[] diaryRewards, String quotaString, long playtime) {
        if (diaryRewards.length > 0) {
            List<Reward> reach = new ArrayList<>();
            List<String> quota = StringUtils.stringToList(quotaString);
            for (Reward reward : diaryRewards) {
                if (reward.getNum() <= playtime && !quota.contains(String.valueOf(reward.getNum()))) {
                    reach.add(reward);
                }
            }
            // 如果有符合条件的奖励
            if (!reach.isEmpty()) {
                Reward playtimeReward = RandomUtils.randomGet(reach);
                boolean handOut = RewardView.handOutReward(player, playtimeReward);
                // 如果发放成功，添加限制
                if (playtimeReward != null && handOut) {
                    quota.add(String.valueOf(playtimeReward.getNum()));
                    return StringUtils.toJsonString(quota);
                }
            }
        }
        return null;
    }
    
}
