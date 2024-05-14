package org.fireflyest.pamphlet.gui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.fireflyest.craftgui.api.View;
import org.fireflyest.pamphlet.service.PamphletService;

public class ProgressView implements View<ProgressPage> {

    private final Map<String, ProgressPage> pageMap = new HashMap<>();

    private final PamphletService service;

    public ProgressView(PamphletService service) {
        this.service = service;
    }

    @Override
    public ProgressPage getFirstPage(@Nullable String target) {
        return pageMap.computeIfAbsent(target, k -> new ProgressPage(target, service));
    }

    @Override
    public void removePage(@Nullable String target) {
        // 
    }
    
}
