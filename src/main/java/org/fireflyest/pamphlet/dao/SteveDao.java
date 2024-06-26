package org.fireflyest.pamphlet.dao;

import org.fireflyest.craftdatabase.annotation.Dao;
import org.fireflyest.craftdatabase.annotation.Delete;
import org.fireflyest.craftdatabase.annotation.Insert;
import org.fireflyest.craftdatabase.annotation.Select;
import org.fireflyest.craftdatabase.annotation.Update;
import org.fireflyest.pamphlet.bean.Steve;

@Dao("org.fireflyest.pamphlet.bean.Steve")
public interface SteveDao {
    
    @Select("SELECT * FROM `pamphlet_steve` WHERE `uid`='${uid}';")
    Steve selectSteveByUid(String uid);

    @Select("SELECT `signed` FROM `pamphlet_steve` WHERE `uid`='${uid}';")
    int selectSteveSignedByUid(String uid);

    @Select("SELECT `series` FROM `pamphlet_steve` WHERE `uid`='${uid}';")
    int selectSteveSeriesByUid(String uid);

    @Select("SELECT `exp` FROM `pamphlet_steve` WHERE `uid`='${uid}';")
    int selectSteveExpByUid(String uid);

    @Select("SELECT `gain` FROM `pamphlet_steve` WHERE `uid`='${uid}';")
    int selectSteveGainByUid(String uid);

    @Select("SELECT `season` FROM `pamphlet_steve` WHERE `uid`='${uid}';")
    int selectSteveSeasonByUid(String uid);

    @Insert("INSERT INTO `pamphlet_steve` (`uid`,`name`,`season`) VALUES ('${uid}','${name}',${season});")
    long insertSteve(String uid, String name, int season);
    
    @Update("UPDATE `pamphlet_steve` SET `gain`=`gain`+1 WHERE `uid`='${uid}';")
    long updateSteveGainAdd(String uid);

    @Update("UPDATE `pamphlet_steve` SET `signed`=`signed`+1 WHERE `uid`='${uid}';")
    long updateSteveSignedAdd(String uid);

    @Update("UPDATE `pamphlet_steve` SET `series`=`series`+1 WHERE `uid`='${uid}';")
    long updateSteveSeriesAdd(String uid);

    @Update("UPDATE `pamphlet_steve` SET `series`=1 WHERE `uid`='${uid}';")
    long updateSteveSeriesReset(String uid);    
    
    @Update("UPDATE `pamphlet_steve` SET `exp`=`exp`+${exp} WHERE `uid`='${uid}';")
    long updateSteveExpAdd(String uid, int exp);
    
    @Update("UPDATE `pamphlet_steve` SET `exp`=0 WHERE `uid`='${uid}';")
    long updateSteveExpReset(String uid);

    @Update("UPDATE `pamphlet_steve` SET `quota`='${quota}' WHERE `uid`='${uid}';")
    long updateSeasonPlaytimeQuota(String uid, String quota);

    @Delete("DELETE FROM `pamphlet_steve` WHERE `uid`='${uid}'")
    long deleteSteveByUid(String uid);

}
