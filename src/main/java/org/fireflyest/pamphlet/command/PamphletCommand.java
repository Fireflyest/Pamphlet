package org.fireflyest.pamphlet.command;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.ComplexCommand;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.pamphlet.Pamphlet;
import org.fireflyest.pamphlet.data.Language;

public class PamphletCommand extends ComplexCommand {

    private final ViewGuide guide;

    public PamphletCommand(ViewGuide guide) {
        this.guide = guide;
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender) {
        Player player = (sender instanceof Player) ? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.ONLY_PLAYER_USE);
            return false;
        }

        guide.openView(player, Pamphlet.VIEW_EXP, player.getUniqueId().toString());

        return true;
    }
    
}
