package org.fireflyest.pamphlet.dao;

import org.fireflyest.craftdatabase.annotation.Dao;
import org.fireflyest.craftdatabase.annotation.Insert;
import org.fireflyest.craftdatabase.annotation.Select;
import org.fireflyest.craftdatabase.annotation.Update;
import org.fireflyest.pamphlet.bean.Steve;

@Dao("org.fireflyest.pamphlet.bean.Steve")
public interface SteveDao {
    
    @Select("SELECT * FROM `pamphlet_steve` WHERE `uid`='${uid}';")
    Steve selectSteveByUid(String uid);

    @Insert("INSERT INTO `pamphlet_steve` (`uid`,`name`,`season`) VALUES ('${uid}','${name}',${season});")
    long insertSteve(String uid, String name, int season);
    
    @Update("UPDATE `pamphlet_steve` SET `signed`=`signed`+1 WHERE `uid`='${uid}';")
    long updateSteveSignedAdd(String uid);

    @Update("UPDATE `pamphlet_steve` SET `series`=`series`+1 WHERE `uid`='${uid}';")
    long updateSteveSeriesAdd(String uid);

    @Update("UPDATE `pamphlet_steve` SET `series`=1 WHERE `uid`='${uid}';")
    long updateSteveSeriesReset(String uid);    

}
