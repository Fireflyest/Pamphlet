package org.fireflyest.pamphlet.bean;

import org.fireflyest.craftdatabase.annotation.Column;
import org.fireflyest.craftdatabase.annotation.ID;
import org.fireflyest.craftdatabase.annotation.Table;

@Table("pamphlet_reward")
public class Reward {
    
    @ID
    @Column
    private long id;

    // 奖励名称
    @Column
    private String name;

    // level sign series_sign cumulative_sign playtime season_playtime
    @Column
    private String type;

    // 奖励所在周目
    @Column
    private int season;

    // 奖励位置
    @Column
    private int num;

    // 展示的物品
    @Column(dataType = "text")
    private String item;

    // 奖励指令
    @Column
    private String commands;

    public Reward(String name, String type, int season, int num, String item) {
        this.name = name;
        this.type = type;
        this.season = season;
        this.num = num;
        this.item = item;
    }

    public Reward() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
