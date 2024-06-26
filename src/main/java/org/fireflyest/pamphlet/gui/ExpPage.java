package org.fireflyest.pamphlet.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
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
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.StringUtils;
import org.fireflyest.util.TimeUtils;

public class ExpPage extends TemplatePage {

    private final PamphletService service;
    private final ViewGuide guide;

    private int selectLevel = -1;
    private int outsetLevel = 1;

    private Steve steve;

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
        steve = service.selectSteveByUid(UUID.fromString(target));
        Validate.notNull(steve, "player uuid:" + target + " data is null!");

        // 获取所有奖励
        Map<Long, ItemStack> rewardMap = new HashMap<>();      
        for (Reward reward : service.selectRewardByType("level", Config.SEASON)) {
            ItemStack rewardItem = SerializationUtil.deserializeItemStack(reward.getItem());
            this.loreItemData(rewardItem, reward);
            rewardMap.put(reward.getNum(), rewardItem);
        }

        int level = steve.getExp() / 100;
        selectLevel = selectLevel == -1 ? level : selectLevel;
        outsetLevel = selectLevel > 6 ? selectLevel - 4 : 1;
        for (int i = outsetLevel, j = 0; i < outsetLevel + 9; i++, j++) {
            // 等级进度按钮
            boolean hasCommand = false;
            Material levelMaterial = Material.BLACK_STAINED_GLASS_PANE;
            if (i <= steve.getGain()) {
                levelMaterial = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            } else if (i <= level) {
                hasCommand = true;
                levelMaterial = Material.LIME_STAINED_GLASS_PANE;
            }
            ButtonItemBuilder levelButtonBuilder = new ButtonItemBuilder(levelMaterial);
            if (hasCommand) {
                levelButtonBuilder.actionPlayerCommand("pamphlet gain").lore("&7点击领取奖励");
            }
            ItemStack levelButton = levelButtonBuilder.name("&aLv&f" + i)
                .lore("&7点击调整显示位置")
                .amount(i)
                .build();
            asyncButtonMap.put(45 + j, levelButton);

            // 奖励
            ItemStack slotItem0 = rewardMap.get(i * 3L);
            if (slotItem0 != null) asyncButtonMap.put(18 + j, slotItem0);
            ItemStack slotItem1 = rewardMap.get(i * 3 + 1L);
            if (slotItem1 != null) asyncButtonMap.put(27 + j, slotItem1);
            ItemStack slotItem2 = rewardMap.get(i * 3 + 2L);
            if (slotItem2 != null) asyncButtonMap.put(36 + j, slotItem2);
        }

        // 右侧玩家操作按钮
        this.addPlayerButton();

        // 左侧导航按钮
        this.addNavigationButton();

        return asyncButtonMap;
    }

    @Override
    public ItemStack getItem(int slot) {
        // 点击等级切换选中位置
        if (slot >= 45 && slot < 45 + 9) {
            selectLevel = outsetLevel + (slot % 45);
            outsetLevel = selectLevel > 6 ? selectLevel - 4 : 1;
            guide.refreshPages(Pamphlet.VIEW_EXP, target);
        }
        return super.getItem(slot);
    }

    @Override
    public void refreshPage() {
        ItemStack blank = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build();
        for (int i = 9; i < 18; i++) {
            buttonMap.put(i, blank);
        }
    }

    
    /**
     * 左侧导航
     */
    private void addNavigationButton() {
        ItemStack expItem = new ItemBuilder(Material.ENCHANTED_BOOK)
            .name("&f[&a手册&f]")
            .lore("&fLv" + (steve.getExp() / 100))
            .lore(StringUtils.stringProgress((steve.getExp() % 100) / 100.0, "&7", "&f", 20))
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
    }

    /**
     * 右侧签到和在线按钮
     */
    private void addPlayerButton() {
        // 右侧签到奖励
        String diaryTarget = target + "-" + TimeUtils.getLocalDate();
        Diary diary = service.selectDiaryByTarget(diaryTarget);
        if (diary == null) {
            diary = new Diary(target, Config.SEASON);
            service.insertDiary(target, Config.SEASON);
        }
        ItemStack signItem;
        if (!diary.isSign()) {
            signItem = new ButtonItemBuilder(Material.WRITABLE_BOOK)
                .actionPlayerCommand("pamphlet sign")
                .name("&f[&a点击签到&f]")
                .lore("&f点击获取每日礼包")
                .build();
        } else {
            signItem = new ItemBuilder(Material.KNOWLEDGE_BOOK)
                .name("&f[&a签到完成&f]")
                .lore(String.format("&f连续签到&9%s&f天", steve.getSeries()))
                .lore(String.format("&f周目总签到&9%s&f天", steve.getSigned()))
                .build();
        }
        asyncButtonMap.put(8, signItem);

        // 右侧在线奖励
        String playtimeKey = Pamphlet.KEY_PLAYER_PLAYTIME + target;
        long todayPlaytime = guide.getViewVariable().age(playtimeKey) * 1000 + NumberConversions.toLong(guide.getViewVariable().get(playtimeKey));
        long seasonPlaytime = service.selectSeasonPlaytime(target, Config.SEASON) + todayPlaytime;
        ItemStack playtimeItem = new ButtonItemBuilder(Material.CLOCK)
            .actionPlayerCommand("pamphlet playtime")
            .name("&f[&a在线奖励&f]")
            .lore(String.format("&f今天在线&9%s", TimeUtils.duration(todayPlaytime)))
            .lore(String.format("&f周目总在线&9%s", TimeUtils.duration(seasonPlaytime)))
            .build();
        asyncButtonMap.put(7, playtimeItem);
    }


    /**
     * 奖励物品注释
     * @param item 物品
     * @param reward 奖励
     */
    private void loreItemData(ItemStack item, Reward reward) {
        // 重命名
        if (reward.getName() != null) {
            ItemUtils.setDisplayName(item, reward.getName());
        }
    }
    
}
