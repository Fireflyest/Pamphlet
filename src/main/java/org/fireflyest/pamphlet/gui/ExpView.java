package org.fireflyest.pamphlet.gui;

import java.util.HashMap;
import java.util.Map;

import org.fireflyest.craftgui.api.View;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.pamphlet.service.PamphletService;

public class ExpView implements View<ExpPage> {

    private final Map<String, ExpPage> pageMap = new HashMap<>();

    private final PamphletService service;
    private final ViewGuide guide;

    public ExpView(PamphletService service, ViewGuide guide) {
        this.service = service;
        this.guide = guide;
    }

    /**
     * target为玩家uuid
     */
    @Override
    public ExpPage getFirstPage(String target) {
        return pageMap.computeIfAbsent(target, k -> new ExpPage(target, service, guide));
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
