package org.fireflyest.pamphlet.gui;

import org.fireflyest.craftgui.api.View;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.pamphlet.service.PamphletService;

public class RewardView implements View<RewardPage> {

    private final PamphletService service;
    private final ViewGuide guide;

    private RewardPage rewardPage;

    public RewardView(PamphletService service, ViewGuide guide) {
        this.service = service;
        this.guide = guide;
    }

    @Override
    public RewardPage getFirstPage(String target) {
        if (rewardPage == null) {
            // target为周目号
            rewardPage = new RewardPage(target, service, guide);
        }
        return rewardPage;
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
