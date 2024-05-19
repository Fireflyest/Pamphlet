package org.fireflyest.pamphlet.dao;

import org.fireflyest.craftdatabase.annotation.Dao;
import org.fireflyest.craftdatabase.annotation.Insert;
import org.fireflyest.craftdatabase.annotation.Select;
import org.fireflyest.craftdatabase.annotation.Update;
import org.fireflyest.pamphlet.bean.Diary;

@Dao("org.fireflyest.pamphlet.bean.Diary")
public interface DiaryDao {
    
    @Select("SELECT * FROM `pamphlet_diary` WHERE `target`='${target}' LIMIT 1;" )
    Diary selectDiaryByTarget(String target);

    @Insert("INSERT INTO `pamphlet_diary` (`target`) VALUES ('${target}');")
    long insertDiary(String target);

    @Update("UPDATE `pamphlet_diary` SET `sign`=1 WHERE `target`='${target}';")
    long updateDiarySign(String target);

}
