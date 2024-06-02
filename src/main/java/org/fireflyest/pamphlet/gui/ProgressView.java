package org.fireflyest.pamphlet.gui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.fireflyest.craftgui.api.View;
import org.fireflyest.pamphlet.data.PamphletYaml;
import org.fireflyest.pamphlet.service.PamphletService;

public class ProgressView implements View<ProgressPage> {

    private final Map<String, ProgressPage> pageMap = new HashMap<>();

    private final PamphletService service;
    private final PamphletYaml yaml;

    public ProgressView(PamphletService service, PamphletYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    /**
     * target应为玩家uuid
     */
    @Override
    public ProgressPage getFirstPage(@Nullable String target) {
        return pageMap.computeIfAbsent(target, k -> new ProgressPage(target, service, yaml));
    }

    @Override
    public void removePage(@Nullable String target) {
        // 
    }
    
}
