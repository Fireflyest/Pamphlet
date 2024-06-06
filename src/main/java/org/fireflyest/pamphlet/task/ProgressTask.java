package org.fireflyest.pamphlet.task;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.fireflyest.crafttask.api.PrepareTask;
import org.fireflyest.crafttask.exception.ExecuteException;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.service.PamphletService;

public class ProgressTask extends PrepareTask {

    private PamphletService service;

    public ProgressTask(@Nonnull String playerName, Object... values) {
        super(playerName, values);
    }

    @Override
    public void execute() throws ExecuteException {
        Validate.notNull(service, "The data service is null!");
        if (values.length == 2) {
            service.updateProgressReachAdd(String.valueOf(values[0]), Config.SEASON, String.valueOf(values[1]));
        }
    }

    public void setService(PamphletService service) {
        this.service = service;
    }
    
}
