package org.fireflyest.pamphlet.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.button.ButtonAction;
import org.fireflyest.craftgui.button.ButtonItemBuilder;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.craftitem.builder.ItemBuilder;
import org.fireflyest.pamphlet.Pamphlet;
import org.fireflyest.pamphlet.bean.Reward;
import org.fireflyest.pamphlet.bean.Season;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.StringUtils;
import org.fireflyest.util.TimeUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RewardPage extends TemplatePage {

    private final PamphletService service;
    private final ViewGuide guide;
    private int seasonId;
    private int rewardType = 0;
    private int selectLevel = -1;
    private int outsetLevel = 1;
    private static final String SLOT_NAME = "&f[&a&l↓&f]";
    private static final String DRAG_TIP = "&7请拖拽物品至此";

    public static final String REWARD_LEVEL = "level";
    public static final String REWARD_SIGN = "sign";
    public static final String REWARD_SERIES_SIGN = "series_sign";
    public static final String REWARD_CUMULATIVE_SIGN = "cumulative_sign";
    public static final String REWARD_PLAYTIME = "playtime";
    public static final String REWARD_SEASON_PLAYTIME = "season_playtime";

    protected RewardPage(String target, PamphletService service, ViewGuide guide) {
        super(Language.TITLE_REWARD, target, 0, 54);
        this.service = service;
        this.guide  = guide;
        this.seasonId = NumberConversions.toInt(target);
        
        this.refreshPage();
    }

    @Override
    public @Nonnull Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        if (rewardType == 0) {
            // 手册奖励
            this.addLevelReward();
        } else if (rewardType == 1) { 
            // 签到奖励
            this.addSignReward();
            this.addBlank();
        } else if (rewardType == 2) { 
            // 在线奖励
            this.addPlaytimeReward();
            this.addBlank();
        }

        // 右侧赛季信息
        Season season = service.selectSeasonById(Config.SEASON);
        if (season != null) {
            ItemStack seasonItem = SerializationUtil.deserializeItemStack(season.getItem());
            ItemUtils.setDisplayName(seasonItem, "&6&l" + season.getName());
            ItemUtils.addLore(seasonItem, "§f" + season.getDesc());
            ItemUtils.addLore(seasonItem, StringUtils.format("§f已有§9{}§f玩家加入当前周目", String.valueOf(season.getPlayers())));
            ItemUtils.addLore(seasonItem, StringUtils.format("§f解锁§9{}§f高级手册", String.valueOf(season.getAdvance())));
            asyncButtonMap.put(8, seasonItem);
        }

        return asyncButtonMap;
    }

    @Override
    public ItemStack getItem(int slot) {
        // 点击等级切换选中位置
        if (slot >= 45 && slot < 45 + 9) {
            selectLevel = outsetLevel + (slot % 45);
            outsetLevel = selectLevel > 6 ? selectLevel - 4 : 1;
        } else if (slot >= 0 && slot <= 2) {
            rewardType = slot;
        }
        guide.refreshPages(Pamphlet.VIEW_REWARD, target);
        return super.getItem(slot);
    }

    @Override
    public void refreshPage() {
        ItemStack blank = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build();
        for (int i = 9; i < 18; i++) {
            buttonMap.put(i, blank);
        }

        // 左侧导航按钮
        ItemStack expItem = new ItemBuilder(Material.ENCHANTED_BOOK)
            .name("&f[&a手册奖励&f]")
            .lore("&7等级达成可领取对应奖励")
            .lore("&7每个等级对应三个奖励")
            .lore("&7下面两个奖励为进阶奖励")
            .build();
        buttonMap.put(0, expItem);
        ItemStack signItem = new ItemBuilder(Material.WRITABLE_BOOK)
            .name("&f[&a签到奖励&f]")
            .lore("&7签到条件达成可获取奖励")
            .lore("&7奖励在奖池中随机抽取")
            .build();
        buttonMap.put(1, signItem);
        ItemStack playtimeItem = new ItemBuilder(Material.CLOCK)
            .name("&f[&a在线奖励&f]")
            .lore("&7在线时长达成可获取奖励")
            .lore("&7奖励在奖池中随机抽取")
            .build();
        buttonMap.put(2, playtimeItem);
        
    }

    /**
     * 添加在线奖励
     */
    private void addPlaytimeReward() {
        List<Reward> playtimeRewardList = Arrays.asList(service.selectRewardByType(REWARD_PLAYTIME, seasonId));
        List<Reward> seasonRewardList = Arrays.asList(service.selectRewardByType(REWARD_SEASON_PLAYTIME, seasonId));
        int index1 = 18;
        int index2 = 23;
        for (int i = 0; i < 12; i++) {
            ItemStack slotItem1;
            if (i < playtimeRewardList.size()) {
                Reward reward = playtimeRewardList.get(i);
                slotItem1 = SerializationUtil.deserializeItemStack(reward.getItem());
                this.loreItemDate(slotItem1, reward);
            } else {
                slotItem1  = new ButtonItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                    .actionPlugin(REWARD_PLAYTIME + " " + (1000 * 60 * 20) + " " + seasonId) // level num season
                    .name(SLOT_NAME)
                    .lore("&7在线奖励")
                    .lore(DRAG_TIP)
                    .build();
            }
            asyncButtonMap.put(index1++, slotItem1);

            ItemStack slotItem2;
            if (i < seasonRewardList.size()) {
                Reward reward = seasonRewardList.get(i);
                slotItem2 = SerializationUtil.deserializeItemStack(reward.getItem());
                this.loreItemDate(slotItem2, reward);
            } else {
                slotItem2 = new ButtonItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                    .actionPlugin(REWARD_SEASON_PLAYTIME + " " + (1000 * 60 * 60 * 48) + " " + seasonId) // level num season
                    .name(SLOT_NAME)
                    .lore("&7周目在线奖励")
                    .lore(DRAG_TIP)
                    .build();
            }
            asyncButtonMap.put(index2++, slotItem2);

            // 换行
            if (index1 % 9 == 4) {
                index1 += 5;
                index2 += 5;
            }
        }

        // 隔板
        ItemStack blank = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build();
        asyncButtonMap.put(22, blank);
        asyncButtonMap.put(22 + 9, blank);
        asyncButtonMap.put(22 + 18, blank);
    }

    /**
     * 添加签到奖励
     */
    private void addSignReward() {    
        // 获取所有奖励，不足的填充插槽
        // 写的和屎山一样
        List<Reward> signRewardList = Arrays.asList(service.selectRewardByType(REWARD_SIGN, seasonId));
        int index0 = 18;
        for (int i = 0; i < 9; i++) {
            ItemStack slotItem0;
            if (i < signRewardList.size()) {
                Reward reward = signRewardList.get(i);
                slotItem0 = SerializationUtil.deserializeItemStack(reward.getItem());
                this.loreItemDate(slotItem0, reward);
            } else {
                 slotItem0 = new ButtonItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                    .actionPlugin(REWARD_SIGN + " 0 " + seasonId) // level num season
                    .name(SLOT_NAME)
                    .lore("&7签到奖励")
                    .lore(DRAG_TIP)
                    .build();
            }
            asyncButtonMap.put(index0++, slotItem0);
            if (index0 % 9 == 3) index0 += 6;
        }

        List<Reward> seriesRewardList = Arrays.asList(service.selectRewardByType(REWARD_SERIES_SIGN, seasonId));
        List<Reward> cumulativeRewardList = Arrays.asList(service.selectRewardByType(REWARD_CUMULATIVE_SIGN, seasonId));
        int index1 = 22;
        int index2 = 25;
        for (int i = 0; i < 6; i++) {
            ItemStack slotItem1;
            if (i < seriesRewardList.size()) {
                Reward reward = seriesRewardList.get(i);
                slotItem1 = SerializationUtil.deserializeItemStack(reward.getItem());
                this.loreItemDate(slotItem1, reward);
            } else {
                slotItem1  = new ButtonItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                    .actionPlugin(REWARD_SERIES_SIGN + " 3 " + seasonId) // level num season
                    .name(SLOT_NAME)
                    .lore("&7连续签到奖励")
                    .lore(DRAG_TIP)
                    .build();
            }
            asyncButtonMap.put(index1++, slotItem1);

            ItemStack slotItem2;
            if (i < cumulativeRewardList.size()) {
                Reward reward = cumulativeRewardList.get(i);
                slotItem2 = SerializationUtil.deserializeItemStack(reward.getItem());
                this.loreItemDate(slotItem2, reward);
            } else {
                slotItem2 = new ButtonItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                    .actionPlugin(REWARD_CUMULATIVE_SIGN + " 7 " + seasonId) // level num season
                    .name(SLOT_NAME)
                    .lore("&7累计签到奖励")
                    .lore(DRAG_TIP)
                    .build();
            }
            asyncButtonMap.put(index2++, slotItem2);

            // 换行
            if (index1 % 9 == 6) {
                index1 += 7;
                index2 += 7;
            }
        }

        // 隔板
        ItemStack blank = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build();
        asyncButtonMap.put(21, blank);
        asyncButtonMap.put(21 + 9, blank);
        asyncButtonMap.put(21 + 18, blank);
        asyncButtonMap.put(24, blank);
        asyncButtonMap.put(24 + 9, blank);
        asyncButtonMap.put(24 + 18, blank);
    }


    /**
     * 添加手册奖励
     */
    private void addLevelReward() {
        Map<Long, ItemStack> rewardMap = new HashMap<>();      
        // 手册奖励
        String type = REWARD_LEVEL;
        // 获取所有奖励
        for (Reward reward : service.selectRewardByType(REWARD_LEVEL, seasonId)) {
            ItemStack rewardItem = SerializationUtil.deserializeItemStack(reward.getItem());
            this.loreItemDate(rewardItem, reward);
            rewardMap.put(reward.getNum(), rewardItem);
        }
        outsetLevel = selectLevel > 6 ? selectLevel - 4 : 1;
        for (int i = outsetLevel, j = 0; i < outsetLevel + 9; i++, j++) {
            // 等级黑玻璃板
            ItemStack levelButton = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name("&aLv&f" + i)
                .lore("&7点击调整显示位置")
                .amount(i)
                .build();
            asyncButtonMap.put(45 + j, levelButton);

            // 奖励插槽
            long num0 = i * 3L;
            ItemStack slotItem0 = rewardMap.get(num0);
            if (slotItem0 == null) {
                slotItem0 = new ButtonItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                    .actionPlugin(type + " " + num0 + " " + seasonId) // level num season
                    .name(SLOT_NAME)
                    .lore("&7基础奖励")
                    .lore(DRAG_TIP)
                    .build();
            }
            asyncButtonMap.put(18 + j, slotItem0);

            long num1 = i * 3 + 1L;
            ItemStack slotItem1 = rewardMap.get(num1);
            if (slotItem1 == null) {
                slotItem1 = new ButtonItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                    .actionPlugin(type + " " + num1 + " " + seasonId) // level num season
                    .name(SLOT_NAME)
                    .lore("&7进阶奖励")
                    .lore(DRAG_TIP)
                    .build();
            }
            asyncButtonMap.put(27 + j, slotItem1);

            long num2 = i * 3 + 2L;
            ItemStack slotItem2 = rewardMap.get(num2);
            if (slotItem2 == null) {
                slotItem2 = new ButtonItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                    .actionPlugin(type + " " + num2 + " " + seasonId) // level num season
                    .name(SLOT_NAME)    
                    .build();
            }
            asyncButtonMap.put(36 + j, slotItem2);

        }
    }

    /**
     * 底部隔板
     */
    private void addBlank() {
        ItemStack blank = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build();
        for (int i = 45; i < 54; i++) {
            asyncButtonMap.put(i, blank);
        }
    }


    /**
     * 奖励物品注释
     * @param item 物品
     * @param reward 奖励
     */
    private void loreItemDate(ItemStack item, Reward reward) {
        // 重命名
        if (reward.getName() != null) {
            ItemUtils.setDisplayName(item, reward.getName());
        }
        // 分割线
        ItemUtils.addLore(item, "");
        ItemUtils.addLore(item, "§e§m·                         ·");
        // 奖励类型
        String rewardTypeString;
        switch (reward.getType()) {
            case REWARD_LEVEL:
                rewardTypeString = String.format("§f[§b手册等级达到%s§f]", reward.getNum() / 3);
                break;
            case REWARD_SIGN:
                rewardTypeString = "§f[§b每日签到§f]";
                break;
            case REWARD_SERIES_SIGN:
                rewardTypeString = String.format("§f[§b连续签到%s天§f]", reward.getNum());
                break;
            case REWARD_CUMULATIVE_SIGN:
                rewardTypeString = String.format("§f[§b累计签到%s天§f]", reward.getNum());
                break;
            case REWARD_PLAYTIME:
                rewardTypeString = String.format("§f[§b当天在线%s§f]", TimeUtils.duration(reward.getNum()));
                break;
            case REWARD_SEASON_PLAYTIME:
                rewardTypeString = String.format("§f[§b周目总在线%s§f]", TimeUtils.duration(reward.getNum()));
                break;
            default:
                rewardTypeString = "§f[]";
                break;
        }
        ItemUtils.addLore(item, rewardTypeString);
        String rewardResult = reward.getCommands() == null ? "§f获取当前物品" : "§f执行以下指令";
        ItemUtils.addLore(item, rewardResult);
        // 奖励指令
        if (reward.getCommands() != null) {
            Gson gson = new Gson();
            List<String> commandsList = gson.fromJson(reward.getCommands(), new TypeToken<List<String>>() {}.getType());
            for (String command : commandsList) {
                ItemUtils.addLore(item, "§f - §7/" + command);
            }
        }
        // 按钮
        ItemUtils.setItemNbt(item, ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE_OPEN);
        ItemUtils.setItemNbt(item, ButtonAction.NBT_VALUE_KEY, Pamphlet.VIEW_EDIT + "." + reward.getId());

    }
    
}
