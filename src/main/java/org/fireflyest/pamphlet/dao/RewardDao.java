package org.fireflyest.pamphlet.dao;

import org.fireflyest.craftdatabase.annotation.Dao;
import org.fireflyest.craftdatabase.annotation.Delete;
import org.fireflyest.craftdatabase.annotation.Insert;
import org.fireflyest.craftdatabase.annotation.Select;
import org.fireflyest.craftdatabase.annotation.Update;
import org.fireflyest.pamphlet.bean.Reward;

@Dao("org.fireflyest.pamphlet.bean.Reward")
public interface RewardDao {
    
    @Select("SELECT * FROM `pamphlet_reward` WHERE `type`='${type}' AND `season`=${season};")
    Reward[] selectRewardByType(String type, int season);

    @Select("SELECT * FROM `pamphlet_reward` WHERE `type`='${type}' AND `season`=${season} ORDER BY RAND() LIMIT 1;")
    Reward selectRewardRandom(String type, int season);

    @Select("SELECT * FROM `pamphlet_reward` WHERE `id`=${id}")
    Reward selectRewardById(long id);

    @Select("SELECT `commands` FROM `pamphlet_reward` WHERE `id`=${id}")
    String selectRewardCommands(long id);

    @Insert("INSERT INTO `pamphlet_reward` (`type`,`season`,`num`,`item`) VALUES ('${type}',${season},${num},'${item}');")
    long insertReward(String type, int season, int num, String item);

    @Update("UPDATE `pamphlet_reward` SET `commands`='${commands}' WHERE `id`=${id};")
    long updateRewardCommands(long id, String commands);

    @Update("UPDATE `pamphlet_reward` SET `name`='${name}' WHERE `id`=${id};")
    long updateRewardName(long id, String name);

    @Update("UPDATE `pamphlet_reward` SET `num`=${num} WHERE `id`=${id};")
    long updateRewardNum(long id, long num);

    @Delete("DELETE FROM `pamphlet_reward` WHERE `id`=${id};")
    long deleteReward(long id);

}
