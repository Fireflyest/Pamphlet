package org.fireflyest.pamphlet.gui;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.button.ButtonAction;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.craftitem.builder.ItemBuilder;
import org.fireflyest.pamphlet.Pamphlet;
import org.fireflyest.pamphlet.bean.Reward;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.TimeUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EditPage extends TemplatePage {

    private final PamphletService service;
    private final ViewGuide guide;

    private String content = "";
    private int editType;

    public static final String TIP_TEXT = "在此输入参数";
    public static final int EDIT_NONE = 0;
    public static final int EDIT_NAME = 1;
    public static final int EDIT_NUM = 2;
    public static final int EDIT_ADD_COMMAND = 3;
    public static final int EDIT_REMOVE_COMMAND = 4;
    public static final int EDIT_REMOVE = 5;

    protected EditPage(String target, PamphletService service, ViewGuide guide) {
        super(Language.TITLE_EDIT, target, 0, 54);
        this.service = service;
        this.guide = guide;

        this.updateTitle(Language.TITLE_EDIT);
        this.refreshPage();
    }

    @Override
    @Nonnull
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        Reward reward = service.selectRewardById(NumberConversions.toInt(target));
        if (reward == null) {
            return asyncButtonMap;
        }
        // 奖励物品
        ItemStack rewardItem = SerializationUtil.deserializeItemStack(reward.getItem());
        this.loreItemDate(rewardItem, reward);
        asyncButtonMap.put(2, rewardItem);

        // 提示按钮
        ItemStack tipItem = new ItemBuilder(Material.WRITABLE_BOOK)
                .name(TIP_TEXT)
                .lore("&7在上方输入框输入参数")
                .lore("&7中间按钮切换编辑内容类型")
                .lore("&7右侧按钮确认更改")
                .build();
        asyncButtonMap.put(0, tipItem);

        // 编辑类型按钮
        ItemStack editItem = null;
        switch (editType) {
            case EDIT_NONE:
                editItem = new ItemBuilder(Material.SPYGLASS)
                        .name("&f[&a仅查看&f]")
                        .lore("&7点击切换编辑类型")
                        .build();
                break;
            case EDIT_NAME:
                editItem = new ItemBuilder(Material.NAME_TAG)
                        .name("&f[&a更改名称&f]")
                        .build();
                break;
            case EDIT_NUM:
                editItem = new ItemBuilder(Material.FEATHER)
                        .name("&f[&a更改数字条件&f]")
                        .build();
                break;
            case EDIT_ADD_COMMAND:
                editItem = new ItemBuilder(Material.CHAIN_COMMAND_BLOCK)
                        .name("&f[&a添加指令&f]")
                        .build();
                break;
            case EDIT_REMOVE_COMMAND:
                editItem = new ItemBuilder(Material.COMMAND_BLOCK)
                        .name("&f[&a删除指令&f]")
                        .build();
                break;
            case EDIT_REMOVE:
                editItem = new ItemBuilder(Material.LAVA_BUCKET)
                        .name("&f[&c删除奖励&f]")
                        .build();
                break;
            default:
                break;
        }
        if (editItem != null) {
            asyncButtonMap.put(1, editItem);
        }

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        //
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        if (slot == 1) {
            editType++;
            if (editType > 5)
                editType = 0;
        } else if (slot == 2) {
            int id = NumberConversions.toInt(target);
            switch (editType) {
                case EDIT_NAME:
                    service.updateRewardName(id, content);
                    break;
                case EDIT_NUM:
                    service.updateRewardNum(id, NumberConversions.toLong(content));
                    break;
                case EDIT_ADD_COMMAND:
                    service.addRewardCommand(id, content);
                    break;
                case EDIT_REMOVE_COMMAND:
                    service.removeRewardCommand(id, content);
                    break;
                case EDIT_REMOVE:
                    service.deleteReward(id);
                    break;
                case EDIT_NONE:
                default:
                    break;
            }
        }
        guide.refreshPages(Pamphlet.VIEW_EDIT, target);
        return super.getItem(slot);
    }

    @Override
    public void updateTitle(String title) {
        this.inventory = Bukkit.createInventory(null, InventoryType.ANVIL, title);
    }

    /**
     * 更新文本内容
     * 
     * @param content 文本内容
     */
    public void updateContent(String content) {
        if (!this.content.equals(content)) {
            this.content = content;
        }
    }

    /**
     * 奖励物品注释
     * 
     * @param item   物品
     * @param reward 奖励
     */
    private void loreItemDate(ItemStack item, Reward reward) {
        // 重命名
        if (editType == EDIT_NAME) {
            ItemUtils.setDisplayName(item, reward.getName() + "&7 → &r" + content);
        } else if (reward.getName() != null) {
            ItemUtils.setDisplayName(item, reward.getName());
        }
        // 分割线
        ItemUtils.addLore(item, editType == EDIT_REMOVE ? "§4点击删除" : "");
        ItemUtils.addLore(item, "§e§m·                         ·");
        // 奖励类型
        String rewardTypeString = this.getRewardTypeString(reward.getType(), reward.getNum());
        // 修改条件数值
        if (editType == EDIT_NUM) {
            int editedNum = NumberConversions.toInt(content);
            rewardTypeString += (" §7→ §4"
                    + (RewardPage.REWARD_LEVEL.equals(reward.getType()) ? editedNum / 3 : editedNum));
        }
        ItemUtils.addLore(item, rewardTypeString);
        String rewardResult = (reward.getCommands() == null && editType != EDIT_ADD_COMMAND) ? "§f获取当前物品" : "§f执行以下指令";
        ItemUtils.addLore(item, rewardResult);

        // 奖励指令
        if (editType == EDIT_ADD_COMMAND) {
            ItemUtils.addLore(item, "§f - §7/§n" + content);
        }
        if (reward.getCommands() != null) {
            Gson gson = new Gson();
            List<String> commandsList = gson.fromJson(reward.getCommands(), new TypeToken<List<String>>() {
            }.getType());
            if (editType == EDIT_REMOVE_COMMAND && commandsList.contains(content)) {
                commandsList.remove(content);
                commandsList.add("§m" + content);
            }
            for (String command : commandsList) {
                ItemUtils.addLore(item, "§f - §7/" + command);
            }
        }
        // 按钮
        ItemUtils.setItemNbt(item, ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE_OPEN);
        ItemUtils.setItemNbt(item, ButtonAction.NBT_VALUE_KEY, Pamphlet.VIEW_EDIT + "." + reward.getId());

    }

    /**
     * 获取奖励条件语句
     * 
     * @param rewardType 奖励获取类型
     * @param rewardNum  奖励获取条件数值
     * @return 奖励条件语句
     */
    private String getRewardTypeString(String rewardType, long rewardNum) {
        String rewardTypeString;
        switch (rewardType) {
            case RewardPage.REWARD_LEVEL:
                rewardTypeString = String.format("§f[§b手册等级达到%s§f]", rewardNum / 3);
                break;
            case RewardPage.REWARD_SIGN:
                rewardTypeString = "§f[§b每日签到§f]";
                break;
            case RewardPage.REWARD_SERIES_SIGN:
                rewardTypeString = String.format("§f[§b连续签到%s天§f]", rewardNum);
                break;
            case RewardPage.REWARD_CUMULATIVE_SIGN:
                rewardTypeString = String.format("§f[§b累计签到%s天§f]", rewardNum);
                break;
            case RewardPage.REWARD_PLAYTIME:
                rewardTypeString = String.format("§f[§b当天在线%s§f]", TimeUtils.duration(rewardNum));
                break;
            case RewardPage.REWARD_SEASON_PLAYTIME:
                rewardTypeString = String.format("§f[§b周目总在线%s§f]", TimeUtils.duration(rewardNum));
                break;
            default:
                rewardTypeString = "§f[]";
                break;
        }
        return rewardTypeString;
    }

}
