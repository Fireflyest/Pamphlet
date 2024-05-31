package org.fireflyest.pamphlet.command;

import java.time.LocalDate;
import java.time.ZoneOffset;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.pamphlet.bean.Season;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.data.PamphletYaml;
import org.fireflyest.pamphlet.service.PamphletService;

public class SeasonAlterCommand extends SubCommand {

    private final PamphletService service;
    private final PamphletYaml yaml;

    public SeasonAlterCommand(PamphletService service, PamphletYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender) {
        return this.execute(sender, String.valueOf(Config.SEASON));
    }

    @Override
    protected boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1) {
        int seasonId = NumberConversions.toInt(arg1);
        Season season = service.selectSeasonById(seasonId);
        if (season == null) {
            sender.sendMessage(Language.SEASON_NULL);
            return false;
        }

        // 当天早上八点为周目起始时间
        service.updateSeasonOutset(seasonId, LocalDate.now().atTime(8, 0).toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
        // 更新config.yml
        yaml.seasonAlter(seasonId);

        Config.setSeason(seasonId);

        return true;
    }
    
}
