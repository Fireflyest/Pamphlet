package org.fireflyest.pamphlet.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.button.ButtonItemBuilder;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.craftitem.builder.ItemBuilder;
import org.fireflyest.pamphlet.Pamphlet;
import org.fireflyest.pamphlet.bean.Diary;
import org.fireflyest.pamphlet.bean.Reward;
import org.fireflyest.pamphlet.bean.Steve;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.TimeUtils;

public class ExpPage extends TemplatePage {

    private final PamphletService service;
    private final ViewGuide guide;

    private int selectLevel = -1;
    private int outsetLevel = 1;

    protected ExpPage(String target, PamphletService service, ViewGuide guide) {
        super(Language.TITLE_EXP, target, 0, 54);
        this.service = service;
        this.guide = guide;

        this.refreshPage();
    }

    @Override
    public @Nonnull Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        // 获取玩家数据
        Steve steve = service.selectSteveByUid(UUID.fromString(target));
        if (steve == null) {
            return asyncButtonMap;
        }
        // 获取所有奖励
        Map<Integer, ItemStack> rewardMap = new HashMap<>();      
        for (Reward reward : service.selectRewardByType("level", Config.SEASON)) {
            ItemStack rewardItem = SerializationUtil.deserializeItemStack(reward.getItem());
            rewardMap.put(reward.getNum(), rewardItem);
        }

        int level = steve.getExp() / 100;
        selectLevel = selectLevel == -1 ? level : selectLevel;
        outsetLevel = selectLevel > 6 ? selectLevel - 4 : 1;
        for (int i = outsetLevel, j = 0; i < outsetLevel + 9; i++, j++) {
            // 等级进度按钮
            Material levelMaterial = Material.BLACK_STAINED_GLASS_PANE;
            if (i <= steve.getGain()) {
                levelMaterial = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            } else if (i <= level) {
                levelMaterial = Material.LIME_STAINED_GLASS_PANE;
            }
            ItemStack levelButton = new ItemBuilder(levelMaterial)
                .name("&aLv&f" + i)
                .lore("&7点击调整显示位置")
                .amount(i)
                .build();
            asyncButtonMap.put(45 + j, levelButton);

            // 奖励
            ItemStack slotItem0 = rewardMap.get(i * 3);
            if (slotItem0 != null) {
                asyncButtonMap.put(18 + j, slotItem0);
            }
            ItemStack slotItem1 = rewardMap.get(i * 3 + 1);
            if (slotItem1 != null) {
                asyncButtonMap.put(27 + j, slotItem1);
            }
           ItemStack slotItem2 = rewardMap.get(i * 3 + 2);
           if (slotItem2 != null) {
                asyncButtonMap.put(36 + j, slotItem2);
           }
        }

        // 右侧签到奖励
        String diaryTarget = target + "-" + TimeUtils.getLocalDate();
        Diary diary = service.selectDiaryByTarget(diaryTarget);
        if (diary == null) {
            diary = new Diary(target);
            service.insertDiary(target);
        }
        ItemStack signItem;
        if (!diary.isSign()) {
            signItem = new ButtonItemBuilder(Material.WRITABLE_BOOK)
                .actionPlayerCommand("pamphlet sign")
                .name("&f[&a点击签到&f]")
                .lore("&7点击获取每日礼包")
                .build();
        } else {
            signItem = new ItemBuilder(Material.KNOWLEDGE_BOOK)
                .name("&f[&a已签到&f]")
                .lore("&7...")
                .build();
        }
        asyncButtonMap.put(8, signItem);
        // 右侧在线奖励
        ItemStack playtimeItem = new ButtonItemBuilder(Material.CLOCK)
            .actionPlayerCommand("pamphlet playtime")
            .name("&f[&a在线奖励&f]")
            .lore("&7...")
            .build();
        asyncButtonMap.put(7, playtimeItem);

        // 左侧导航按钮
        ItemStack expItem = new ItemBuilder(Material.ENCHANTED_BOOK)
            .name("&f[&a手册&f]")
            .build();
        asyncButtonMap.put(0, expItem);
        ItemStack progressItem = new ButtonItemBuilder(Material.ANVIL)
            .actionOpenPage("pamphlet.progress." + target)
            .name("&f[&a历练&f]")
            .build();
        asyncButtonMap.put(1, progressItem);
        ItemStack exchangeItem = new ButtonItemBuilder(Material.ITEM_FRAME)
            .actionOpenPage("pamphlet.exchange." + target)
            .name("&f[&a兑换&f]")
            .build();
        asyncButtonMap.put(2, exchangeItem);

        return asyncButtonMap;
    }

    @Override
    public ItemStack getItem(int slot) {
        // 点击等级切换选中位置
        if (slot >= 45 && slot < 45 + 9) {
            selectLevel = outsetLevel + (slot % 45);
            outsetLevel = selectLevel > 6 ? selectLevel - 4 : 1;
        }
        guide.refreshPages(Pamphlet.VIEW_EXP, target);
        return super.getItem(slot);
    }

    @Override
    public void refreshPage() {
        ItemStack blank = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build();
        for (int i = 9; i < 18; i++) {
            buttonMap.put(i, blank);
        }
    }
    
}
