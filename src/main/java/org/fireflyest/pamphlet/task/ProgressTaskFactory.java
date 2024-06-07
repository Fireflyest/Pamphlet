package org.fireflyest.pamphlet.task;

import java.util.List;

import javax.annotation.Nonnull;

import org.fireflyest.crafttask.api.TaskFactory;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.StringUtils;

public class ProgressTaskFactory extends TaskFactory<ProgressTask> {

    private final PamphletService service;

    public ProgressTaskFactory(PamphletService service) {
        this.service = service;
    }

    /**
     * playerName uuid type
     */
    @Override
    public ProgressTask create(@Nonnull String value) {
        List<String> valueList = StringUtils.stringToList(value);
        String playerName = valueList.get(0);
        String uid = valueList.get(1);
        String type = valueList.get(2);
        ProgressTask progressTask = new ProgressTask(playerName, uid, type);
        progressTask.setService(service);
        return progressTask;
    }
    
}
