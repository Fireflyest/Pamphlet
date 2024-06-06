package org.fireflyest.pamphlet.task;

import javax.annotation.Nonnull;

import org.fireflyest.crafttask.api.TaskFactory;
import org.fireflyest.pamphlet.service.PamphletService;

public class ProgressTaskFactory extends TaskFactory<ProgressTask> {

    private final PamphletService service;

    public ProgressTaskFactory(PamphletService service) {
        this.service = service;
    }

    /**
     * uuid type
     */
    @Override
    public ProgressTask create(@Nonnull String playerName, Object... values) {
        ProgressTask progressTask = new ProgressTask(playerName, values);
        progressTask.setService(service);
        return progressTask;
    }
    
}
