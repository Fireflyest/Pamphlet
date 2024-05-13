package org.fireflyest.pamphlet.gui;

import java.util.HashMap;
import java.util.Map;

import org.fireflyest.craftgui.api.View;
import org.fireflyest.pamphlet.service.PamphletService;

public class ExchangeView implements View<ExchangePage> {

    private final Map<String, ExchangePage> pageMap = new HashMap<>();

    private final PamphletService service;

    public ExchangeView(PamphletService service) {
        this.service = service;
    }

    @Override
    public ExchangePage getFirstPage(String target) {
        return pageMap.computeIfAbsent(target, k -> new ExchangePage(target, service));
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
