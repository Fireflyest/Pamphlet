package org.fireflyest.pamphlet.gui;

import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.button.ButtonItemBuilder;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.StringUtils;

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

        this.addNavigationButton();

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        // 
    }
    
    /**
     * 左侧导航
     */
    private void addNavigationButton() {
        ItemStack expItem = new ButtonItemBuilder(Material.ENCHANTED_BOOK)
            .actionOpenPage("pamphlet.exp." + target)
            .name("&f[&a&l手册&f]")
            // .lore("&fLv" + (steve.getExp() / 100))
            // .lore(StringUtils.stringProgress((steve.getExp() % 100) / 100.0, "&7", "&a", 20))
            .build();
        asyncButtonMap.put(0, expItem);
        ItemStack progressItem = new ButtonItemBuilder(Material.ANVIL)
            .actionOpenPage("pamphlet.progress." + target)
            .name("&f[&a&l历练&f]")
            .build();
        asyncButtonMap.put(1, progressItem);
        ItemStack exchangeItem = new ButtonItemBuilder(Material.ITEM_FRAME)
            .actionOpenPage("pamphlet.exchange." + target)
            .name("&f[&a&l兑换&f]")
            .build();
        asyncButtonMap.put(2, exchangeItem);
    }

}
