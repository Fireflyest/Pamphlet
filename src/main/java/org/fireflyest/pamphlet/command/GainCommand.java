package org.fireflyest.pamphlet.command;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.pamphlet.bean.Reward;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.gui.RewardPage;
import org.fireflyest.pamphlet.gui.RewardView;
import org.fireflyest.pamphlet.service.PamphletService;

public class GainCommand extends SubCommand {

    private final PamphletService service;
    
    public GainCommand(PamphletService service) {
        this.service = service;
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

        // 是否达到领取
        int level = service.selectSteveExpByUid(player.getUniqueId()) / 100;
        int gain = service.selectSteveGainByUid(player.getUniqueId());
        if (gain < level) {
            // 发放下一等级奖励
            long next = gain + 1L;
            Reward levelReward1 = service.selectRewardRandom(RewardPage.REWARD_LEVEL, "=", next * 3, Config.SEASON);
            Reward levelReward2 = service.selectRewardRandom(RewardPage.REWARD_LEVEL, "=", next * 3 + 1, Config.SEASON);
            Reward levelReward3 = service.selectRewardRandom(RewardPage.REWARD_LEVEL, "=", next * 3 + 2, Config.SEASON);
            int update = 0;
            if (levelReward1 != null && RewardView.handOutReward(player, levelReward1)) update++;
            if (levelReward2 != null && RewardView.handOutReward(player, levelReward2)) update++;
            if (levelReward3 != null && RewardView.handOutReward(player, levelReward3)) update++;
            if (update > 0) {
                // 领取到任何一个奖励就算过等级
                service.updateSteveGainAdd(player.getUniqueId());
            }
        }
        return true;
    }
    
}
