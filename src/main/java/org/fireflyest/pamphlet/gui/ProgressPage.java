package org.fireflyest.pamphlet.gui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;

public class ProgressPage extends TemplatePage {

    Map<String, String> taskName = new HashMap<>();

    private final PamphletService service;

    protected ProgressPage(String target, PamphletService service) {
        super(Language.TITLE_PROGRESS, target, 0, 54);
        this.service = service;

        taskName.put("fishing", "钓鱼");
        taskName.put("mining", "挖矿");
        taskName.put("trade", "村民交易");

        this.refreshPage();
    }

    @Override
    public @Nonnull Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        //
    }
    
}
