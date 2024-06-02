package org.fireflyest.pamphlet.dao;

import org.fireflyest.craftdatabase.annotation.Dao;
import org.fireflyest.craftdatabase.annotation.Insert;
import org.fireflyest.craftdatabase.annotation.Select;
import org.fireflyest.craftdatabase.annotation.Update;
import org.fireflyest.pamphlet.bean.Progress;

@Dao("org.fireflyest.pamphlet.bean.Progress")
public interface ProgressDao {
    
    @Select("SELECT * FROM `pamphlet_progress` WHERE `uid`='${uid}' AND `stage`='${stage}' AND `season`=${season} AND `type`='${type}';")
    Progress selectProgresses(String uid, int stage, int season, String type);

    @Insert("INSERT INTO `pamphlet_progress` (`uid`,`stage`,`season`,`type`) VALUES ('${uid}',${stage},${season},'${type}');")
    long insertProgress(String uid, int stage, int season, String type);

    @Update("UPDATE `pamphlet_progress` SET `reach`=`reach`+1 WHERE `uid`='${uid}' AND `season`=${season} AND `type`='${type}';")
    long updateProgressReachAdd(String uid, int season, String type);
    
}
