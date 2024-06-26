package org.fireflyest.pamphlet.gui;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.button.ButtonItemBuilder;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.craftitem.builder.ItemBuilder;
import org.fireflyest.pamphlet.bean.Progress;
import org.fireflyest.pamphlet.bean.Steve;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.data.PamphletYaml;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.StringUtils;
import org.fireflyest.util.TimeUtils;

public class ProgressPage extends TemplatePage {

    private static final Map<String, String> progressName = new HashMap<>();
    private static final Map<String, Material> progressMaterial = new HashMap<>();

    public static final String PROGRESS_FISH = "fishing";
    public static final String PROGRESS_MINING = "mining";
    public static final String PROGRESS_TRADE = "trade";

    static {
        progressName.put(PROGRESS_FISH, "钓鱼");
        progressName.put(PROGRESS_MINING, "挖矿");
        progressName.put(PROGRESS_TRADE, "村民交易");

        progressMaterial.put(PROGRESS_FISH, Material.FISHING_ROD);
        progressMaterial.put(PROGRESS_MINING, Material.IRON_PICKAXE);
        progressMaterial.put(PROGRESS_TRADE, Material.EMERALD);
    }

    private final PamphletService service;
    private final PamphletYaml yaml;

    private Steve steve;

    protected ProgressPage(String target, PamphletService service, PamphletYaml yaml) {
        super(Language.TITLE_PROGRESS, target, 0, 54);
        this.service = service;
        this.yaml = yaml;

        this.refreshPage();
    }

    @Override
    public @Nonnull Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        Instant seasonOutset = TimeUtils.getInstant(service.selectSeasonOutset(Config.SEASON));
        long days = Duration.between(seasonOutset, Instant.now()).toDays();

        // 获取玩家数据
        steve = service.selectSteveByUid(UUID.fromString(target));
        Validate.notNull(steve, "player uuid:" + target + " data is null!");

        // 四个阶段的任务进度
        this.addProgressButton("stage1", 9 * 2);
        if (days > 30) {
            this.addProgressButton("stage2", 9 * 3);
        }
        if (days > 60) {
            this.addProgressButton("stage3", 9 * 4);
        }
        if (days > 90) {
            this.addProgressButton("stage4", 9 * 5);
        }

        // 左侧导航按钮
        this.addNavigationButton();

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        ItemStack blank = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name(" ").build();
        for (int i = 9; i < 18; i++) {
            buttonMap.put(i, blank);
        }
        ItemStack black = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("&f未开启").build();
        for (int i = 18; i < 54; i++) {
            buttonMap.put(i, black);
        }
    }

    /**
     * 任务进度
     * @param stage 阶段
     */
    private void addProgressButton(String stage, int index) {
        int stageNum = NumberConversions.toInt(stage.replace("stage", ""));
        for (String key : yaml.getProgress().getConfigurationSection(stage).getKeys(false)) {
            // 获取玩家进度
            Progress progress = service.selectProgresses(target, stageNum, Config.SEASON, key);
            if (progress == null) {
                // 新进度数据
                progress = new Progress(target, stageNum, Config.SEASON, key);
                service.insertProgress(progress.getUid(), progress.getStage(), progress.getSeason(), progress.getType());
            }
            // 读取任务信息
            int max = yaml.getProgress().getInt(StringUtils.format("{}.{}", stage, key));
            Material material = progressMaterial.get(key);
            if (material == null) {
                material = Material.BOOK;
            }
            String name = progressName.get(key);
            int reach = progress.getReach();
            ItemStack item = null;
            if (reach == -1) { // 用-1来表示任务完成已领取奖励
                item = new ItemBuilder(material)
                    .name("&e&l" + name)
                    .lore("&a✔")
                    .flags(ItemFlag.HIDE_ATTRIBUTES)
                    .build();
            } else {
                item = new ItemBuilder(material)
                    .name("&e&l" + name)
                    .lore(StringUtils.format("&f{}/{}", String.valueOf(reach), String.valueOf(max)))
                    .lore(StringUtils.stringProgress((reach % max) / (double)max, "&7", "&a", 20))
                    .flags(ItemFlag.HIDE_ATTRIBUTES)
                    .build();
            }
            asyncButtonMap.put(index++, item);
        }
    }


    /**
     * 左侧导航
     */
    private void addNavigationButton() {
        ItemStack expItem = new ButtonItemBuilder(Material.ENCHANTED_BOOK)
            .actionOpenPage("pamphlet.exp." + target)
            .name("&f[&a&l手册&f]")
            .lore("&fLv" + (steve.getExp() / 100))
            .lore(StringUtils.stringProgress((steve.getExp() % 100) / 100.0, "&7", "&a", 20))
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
