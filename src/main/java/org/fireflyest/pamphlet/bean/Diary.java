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

    public Diary(String target) {
        this.target = target;
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

}
