package org.fireflyest.pamphlet.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.fireflyest.crafttask.api.TaskHandler;
import org.fireflyest.pamphlet.Pamphlet;
import org.fireflyest.pamphlet.gui.ProgressPage;
import org.fireflyest.util.StringUtils;

public class TaskEventListener implements Listener {
    
    private final TaskHandler handler;

    public TaskEventListener(TaskHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            String[] value = {player.getName(), player.getUniqueId().toString(), ProgressPage.PROGRESS_FISH};
            handler.runTaskAsynchronously(Pamphlet.getPlugin(), Pamphlet.TASK_PROGRESS, StringUtils.toJsonString(value));
        }
    }

}
