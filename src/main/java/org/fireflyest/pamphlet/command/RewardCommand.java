package org.fireflyest.pamphlet.command;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.pamphlet.Pamphlet;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;

public class RewardCommand extends SubCommand {

    private final ViewGuide guide;

    public RewardCommand(ViewGuide guide) {
        this.guide = guide;
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender) {
        return this.execute(sender, String.valueOf(Config.SEASON));
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1) {
        Player player = (sender instanceof Player) ? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.ONLY_PLAYER_USE);
            return false;
        }

        // 是否有权限
        if (!sender.hasPermission("pamphlet.reward")) {
            sender.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "pamphlet.reward"));
            return true;
        }

        guide.openView(player, Pamphlet.VIEW_REWARD, arg1);
        return true;
    }
    
}
