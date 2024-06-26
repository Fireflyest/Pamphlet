package org.fireflyest.pamphlet.bean;

import org.fireflyest.craftdatabase.annotation.Column;
import org.fireflyest.craftdatabase.annotation.ID;
import org.fireflyest.craftdatabase.annotation.Table;

/**
 * 玩家小任务进度
 */
@Table("pamphlet_progress")
public class Progress {
    
    @ID
    @Column
    private int id;

    // 归属玩家
    @Column
    private String uid;

    @Column
    private int season;

    @Column
    private int stage;

    // 任务类型
    @Column
    private String type;

    // 当前进度
    @Column(defaultValue = "0")
    private int reach;

    public Progress() {
    }

    public Progress(String uid, int stage, int season, String type) {
        this.uid = uid;
        this.stage = stage;
        this.season = season;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

}
