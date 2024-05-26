package org.fireflyest.pamphlet.bean;

import org.fireflyest.craftdatabase.annotation.Column;
import org.fireflyest.craftdatabase.annotation.Primary;
import org.fireflyest.craftdatabase.annotation.Table;

@Table("pamphlet_diary")
public class Diary {
    
    // PlayerUUID-Time
    @Primary
    @Column
    private String target;

    @Column(defaultValue = "0")
    private boolean sign;

    @Column(defaultValue = "0")
    private long playtime;

    // 在线奖励记录
    @Column(defaultValue = "[]")
    private String quota;

    @Column(defaultValue = "0")
    private int season;

    public Diary(String target, int season) {
        this.target = target;
        this.season = season;
    }

    public Diary() {
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String name) {
        this.target = name;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

}
