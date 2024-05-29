package org.fireflyest.pamphlet.command;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.service.PamphletService;

public class SeasonResetCommand extends SubCommand {

    private final PamphletService service;

    public SeasonResetCommand(PamphletService service) {
        this.service = service;
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender) {
        return false;
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1) {
        Player player = Bukkit.getPlayerExact(arg1);
        if (player == null) {
            return false;
        }
        // 清除赛季数据
        int playerSeason = service.selectSteveSeasonByUid(player.getUniqueId());
        if (playerSeason < Config.SEASON) {
            service.deleteSteveByUid(player.getUniqueId());
        }
        // 插入新的数据
        service.insertSteve(player.getUniqueId(), player.getName(), Config.SEASON);
        // 周目统计
        service.updateSeasonPlayersAdd(Config.SEASON);
        return true;
    }
    
}
