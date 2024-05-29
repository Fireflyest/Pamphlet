package org.fireflyest.pamphlet.bean;

import org.fireflyest.craftdatabase.annotation.Column;
import org.fireflyest.craftdatabase.annotation.ID;
import org.fireflyest.craftdatabase.annotation.Table;

@Table("pamphlet_season")
public class Season {
    
    @ID
    @Column
    private int id;

    @Column
    private String name;
    
    @Column
    private String desc;
    
    // 开始日期
    @Column
    private long outset;

    // 初始化按钮
    @Column(dataType = "TEXT")
    private String item;

    // 进入此周目的玩家
    @Column(defaultValue = "0")
    private int players;

    // 获取进阶手册的玩家
    @Column(defaultValue = "0")
    private int advance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getOutset() {
        return outset;
    }

    public void setOutset(long outset) {
        this.outset = outset;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getAdvance() {
        return advance;
    }

    public void setAdvance(int advance) {
        this.advance = advance;
    }

}
