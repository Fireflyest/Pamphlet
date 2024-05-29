package org.fireflyest.pamphlet.command;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.pamphlet.service.PamphletService;

public class SeasonAlterCommand extends SubCommand {

    private final PamphletService service;

    public SeasonAlterCommand(PamphletService service) {
        this.service = service;
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender) {
        return true;
    }
    
}
