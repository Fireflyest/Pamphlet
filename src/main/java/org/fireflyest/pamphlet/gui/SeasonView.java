package org.fireflyest.pamphlet.gui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.fireflyest.craftgui.api.View;
import org.fireflyest.pamphlet.service.PamphletService;

public class SeasonView implements View<SeasonPage> {

    private final Map<String, SeasonPage> pageMap = new HashMap<>();

    private final PamphletService service;
    
    public SeasonView(PamphletService service) {
        this.service = service;
    }

    /**
     * target为玩家uuid
     */
    @Override
    @Nullable
    public SeasonPage getFirstPage(@Nullable String target) {
        return pageMap.computeIfAbsent(target, k -> new SeasonPage(target, service));
    }

    @Override
    public void removePage(@Nullable String target) {
        // 
    }
    
}
