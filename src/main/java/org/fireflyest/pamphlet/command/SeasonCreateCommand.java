package org.fireflyest.pamphlet.command;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;

public class SeasonCreateCommand extends SubCommand {

    private final PamphletService service;

    public SeasonCreateCommand(PamphletService service) {
        this.service = service;
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender) {
        return this.execute(sender, "未命名");
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1) {
        Player player = (sender instanceof Player) ? (Player)sender : null;
        if (player == null) {
            sender.sendMessage(Language.ONLY_PLAYER_USE);
            return false;
        }

        ItemStack seasonItem = player.getInventory().getItemInMainHand();
        if (seasonItem == null || seasonItem.getType().equals(Material.AIR)) {
            seasonItem = new ItemStack(Material.STONE);
        }

        service.insertSeason(arg1, seasonItem);

        sender.sendMessage(Language.SEASON_CREATE);

        return true;
    }
    
}
