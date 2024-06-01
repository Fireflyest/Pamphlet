package org.fireflyest.pamphlet.task;

import org.fireflyest.crafttask.api.PrepareTask;
import org.fireflyest.crafttask.exception.ExecuteException;

public class ProgressTask extends PrepareTask {

    protected ProgressTask(String playerName, String value) {
        super(playerName, value);
    }

    @Override
    public void execute() throws ExecuteException {
        
    }
    
}
