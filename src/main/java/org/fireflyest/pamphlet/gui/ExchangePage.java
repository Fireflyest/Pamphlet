package org.fireflyest.pamphlet.gui;

import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;

public class ExchangePage extends TemplatePage {

    private final PamphletService service;

    protected ExchangePage(String target, PamphletService service) {
        super(Language.TITLE_EXCHANGE, target, 0, 54);
        this.service = service;

        this.refreshPage();
    }

    @Override
    public @Nonnull  Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        // 
    }
    
}
