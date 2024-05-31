package org.fireflyest.pamphlet.command;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.ComplexCommand;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.pamphlet.Pamphlet;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;

public class PamphletCommand extends ComplexCommand {

    private final PamphletService service;
    private final ViewGuide guide;

    public PamphletCommand(PamphletService service, ViewGuide guide) {
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

        String view = service.selectSteveSeasonByUid(player.getUniqueId()) != Config.SEASON ? Pamphlet.VIEW_SEASON : Pamphlet.VIEW_EXP;

        guide.openView(player, view, player.getUniqueId().toString());

        return true;
    }
    
}
