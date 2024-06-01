package org.fireflyest.pamphlet.task;

import org.fireflyest.crafttask.api.TaskFactory;

public class ProgressTaskFactory extends TaskFactory<ProgressTask> {

    @Override
    public ProgressTask create(String value) {
        // TODO: 
        return new ProgressTask(value, value);
    }
    
}
