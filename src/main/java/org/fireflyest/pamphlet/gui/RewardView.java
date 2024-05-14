package org.fireflyest.pamphlet.gui;

import javax.annotation.Nullable;

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
    public RewardPage getFirstPage(@Nullable String target) {
        if (rewardPage == null) {
            // target为周目号
            rewardPage = new RewardPage(target, service, guide);
        }
        return rewardPage;
    }

    @Override
    public void removePage(@Nullable String target) {
        // 
    }
    
}
