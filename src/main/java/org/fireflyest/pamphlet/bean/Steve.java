package org.fireflyest.pamphlet.bean;

import org.fireflyest.craftdatabase.annotation.Column;
import org.fireflyest.craftdatabase.annotation.Primary;
import org.fireflyest.craftdatabase.annotation.Table;

@Table("pamphlet_steve")
public class Steve {
    
    @Primary
    @Column
    private String uid;

    @Column
    private String name;

    // 周目
    @Column(defaultValue = "0")
    private int season;

    // 手册经验
    @Column(defaultValue = "0")
    private int exp;

    // 已领取等级
    @Column(defaultValue = "0")
    private int gain;

    // 总签到次数
    @Column(defaultValue = "0")
    private int signed;

    // 连续签到
    @Column(defaultValue = "0")
    private int series;

    public Steve(String uid, String name, int season) {
        this.uid = uid;
        this.name = name;
        this.season = season;
    }

    public Steve() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getGain() {
        return gain;
    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public int getSigned() {
        return signed;
    }

    public void setSigned(int signed) {
        this.signed = signed;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

}
