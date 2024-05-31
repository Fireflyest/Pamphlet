package org.fireflyest.pamphlet.gui;

import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.button.ButtonAction;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.pamphlet.bean.Season;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.SerializationUtil;

public class SeasonPage extends TemplatePage {

    private final PamphletService service;

    protected SeasonPage(String target, PamphletService service) {
        super(Language.TITLE_SEASON, target, 0, 54);
        this.service = service;
    }

    @Override
    @Nonnull
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        Season season = service.selectSeasonById(Config.SEASON);
        if (season != null) {
            ItemStack seasonItem = SerializationUtil.deserializeItemStack(season.getItem());
            ItemUtils.setDisplayName(seasonItem, "&6&l" + season.getName());
            ItemUtils.addLore(seasonItem, "§f" + season.getDesc());
            ItemUtils.setItemNbt(seasonItem, ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_CONSOLE_COMMAND_SEND);
            ItemUtils.setItemNbt(seasonItem, ButtonAction.NBT_VALUE_KEY, "season reset " + target);
            asyncButtonMap.put(22, seasonItem);
        }

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        //
    }
    
}
