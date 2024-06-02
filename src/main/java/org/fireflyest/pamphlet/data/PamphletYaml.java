package org.fireflyest.pamphlet.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.craftdatabase.yaml.YamlService;
import org.fireflyest.craftitem.builder.ItemBuilder;

public class PamphletYaml extends YamlService {

    private Map<String, ItemBuilder> itemMap = new HashMap<>();
    private FileConfiguration progress;

    public PamphletYaml(JavaPlugin plugin) {
        super(plugin);
        this.setupConfig(Config.class);
        this.setupLanguage(Language.class, Config.LANGUAGE);
        this.setupItems(itemMap);
        this.progress = this.loadYamlFile("progress");
    }

    /**
     * 获取物品
     */
    public ItemBuilder getItemBuilder(String name) {
        return itemMap.get(name);
    }

    /**
     * 周目更替更新config.yml文件
     * @param season 周目id
     */
    public void seasonAlter(int season) {
        this.setConfigData("Season", season);
    }

    /**
     * 获取四个阶段的任务进度
     * @return 文件progress.yml
     */
    public FileConfiguration getProgress() {
        return progress;
    }

}
