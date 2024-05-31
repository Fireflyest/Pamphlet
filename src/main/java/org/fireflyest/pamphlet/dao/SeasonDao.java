package org.fireflyest.pamphlet.dao;

import org.fireflyest.craftdatabase.annotation.Dao;
import org.fireflyest.craftdatabase.annotation.Insert;
import org.fireflyest.craftdatabase.annotation.Select;
import org.fireflyest.craftdatabase.annotation.Update;
import org.fireflyest.pamphlet.bean.Season;

@Dao("org.fireflyest.pamphlet.bean.Season")
public interface SeasonDao {

    @Select("SELECT * FROM `pamphlet_season` WHERE id=${id};")
    Season selectSeasonById(int id);

    @Select("SELECT `id` FROM `pamphlet_season` LIMIT 30;")
    int[] selectSeasonIds();

    @Insert("INSERT INTO `pamphlet_season` (`name`,`item`) VALUES ('${name}','${item}');")
    long insertSeason(String name, String item);

    @Update("UPDATE `pamphlet_season` SET `desc`='${desc}' WHERE `id`=${id};")
    long updateSeasonDesc(int id, String desc);

    @Update("UPDATE `pamphlet_season` SET `outset`=${outset} WHERE `id`=${id};")
    long updateSeasonOutset(int id, long outset);

    @Update("UPDATE `pamphlet_season` SET `players`=`players`+1 WHERE `id`=${id};")
    long updateSeasonPlayersAdd(int id);

    @Update("UPDATE `pamphlet_season` SET `advance`=`advance`+1 WHERE `id`=${id};")
    long updateSeasonAdvanceAdd(int id);

}
