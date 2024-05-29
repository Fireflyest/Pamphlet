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
    
    @Insert("INSERT INTO `pamphlet_season` (`name`,`item`) VALUES ('${name}','${item}');")
    long insertSeason(String name, String item);

    @Update("UPDATE `pamphlet_season` SET `desc`='${desc}' WHERE `id`=${id};")
    long updateSeasonDesc(int id, String desc);

    @Update("UPDATE `pamphlet_season` SET `outset`=${outset} WHERE `id`=${id};")
    long updateSeasonOutset(int id, long outset);

}
