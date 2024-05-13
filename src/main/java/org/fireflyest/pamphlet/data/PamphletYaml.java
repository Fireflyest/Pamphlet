package org.fireflyest.pamphlet.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.craftdatabase.yaml.YamlService;
import org.fireflyest.craftitem.builder.ItemBuilder;

public class PamphletYaml extends YamlService {

    private Map<String, ItemBuilder> itemMap = new HashMap<>();

    public PamphletYaml(JavaPlugin plugin) {
        super(plugin);
        this.setupConfig(Config.class);
        this.setupLanguage(Language.class, Config.LANGUAGE);
        this.setupItems(itemMap);
    }

    /**
     * 获取物品
     */
    public ItemBuilder getItemBuilder(String name) {
        return itemMap.get(name);
    }
    
}
