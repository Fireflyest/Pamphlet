package org.fireflyest.pamphlet.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.fireflyest.craftcommand.argument.Argument;
import org.fireflyest.pamphlet.service.PamphletService;

public class SeasonArgument implements Argument {

    private final PamphletService service;

    public SeasonArgument(PamphletService service) {
        this.service = service;
    }

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        List<String> ret = new ArrayList<>();
        for (int id : service.selectSeasonIds()) {
            String idString = String.valueOf(id);
            if (idString.startsWith(arg)) {
                ret.add(idString);
            }
        }
        return ret;
    }
    
}
