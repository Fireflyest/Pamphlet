package org.fireflyest.pamphlet.dao;

import org.fireflyest.craftdatabase.annotation.Dao;
import org.fireflyest.craftdatabase.annotation.Insert;
import org.fireflyest.craftdatabase.annotation.Select;
import org.fireflyest.craftdatabase.annotation.Update;
import org.fireflyest.pamphlet.bean.Diary;

@Dao("org.fireflyest.pamphlet.bean.Diary")
public interface DiaryDao {
    
    @Select("SELECT * FROM `pamphlet_diary` WHERE `target`='${target}';" )
    Diary selectDiaryByTarget(String target);

    @Select("SELECT SUM(playtime) FROM `pamphlet_diary` WHERE `season`=${season} AND `target` LIKE '${uid}%';" )
    long selectSeasonPlaytime(String uid, int season);

    @Insert("INSERT INTO `pamphlet_diary` (`target`,`season`) VALUES ('${target}',${season});")
    long insertDiary(String target, int season);

    @Update("UPDATE `pamphlet_diary` SET `sign`=1 WHERE `target`='${target}';")
    long updateDiarySign(String target);

    @Update("UPDATE `pamphlet_diary` SET `playtime`=`playtime`+${playtime} WHERE `target`='${target}';")
    long updateDiaryPlaytimeAdd(String target, long playtime);

}
