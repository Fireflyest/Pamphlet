package org.fireflyest.pamphlet.command;

import java.time.LocalDate;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.pamphlet.bean.Diary;
import org.fireflyest.pamphlet.bean.Reward;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.gui.RewardView;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.TimeUtils;

public class SignCommand extends SubCommand {

    private final PamphletService service;
    private final ViewGuide guide;

    public SignCommand(PamphletService service, ViewGuide guide) {
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

        // 今天数据
        String diaryTarget = player.getUniqueId().toString() + "-" + TimeUtils.getLocalDate();
        Diary todayDiary = service.selectDiaryByTarget(diaryTarget);

        // 如果当天没有数据先插入
        if (todayDiary == null) {
            todayDiary = new Diary(diaryTarget, Config.SEASON);
            service.insertDiary(diaryTarget, Config.SEASON);
        }

        // 当天已签
        if (todayDiary.isSign()) {
            sender.sendMessage(Language.SIGN_ALREADY);
            // 刷新页面
            guide.refreshPage(sender.getName());
            return true;
        }

        // 签到
        service.updateDiarySign(diaryTarget);
        sender.sendMessage(Language.SIGN_SUCCESS);
        // 签到奖励
       Reward reward = service.selectRewardRandom("sign", Config.SEASON);
       RewardView.handOutReward(player, reward);

        // 累计签到
        service.updateSteveSignedAdd(player.getUniqueId());
        // TODO: 累计签到奖励

        // 昨天数据用于判断是否连续签到
        String yesterdayTarget = player.getUniqueId().toString() + "-" + LocalDate.now().plusDays(-1).toString();
        Diary yesterdayDiary = service.selectDiaryByTarget(yesterdayTarget);
        // 连续签到
        if (yesterdayDiary != null && yesterdayDiary.isSign()) {
            service.updateSteveSeriesAdd(player.getUniqueId());

            // TODO: 连续签到奖励

        } else {
            service.updateSteveSeriesReset(player.getUniqueId());
        }

        // 刷新页面
        guide.refreshPage(sender.getName());

        return true;
    }
    
}
