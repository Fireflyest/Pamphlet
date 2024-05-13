package org.fireflyest.pamphlet.dao;

import org.fireflyest.craftdatabase.annotation.Dao;
import org.fireflyest.craftdatabase.annotation.Insert;
import org.fireflyest.craftdatabase.annotation.Select;
import org.fireflyest.craftdatabase.annotation.Update;
import org.fireflyest.pamphlet.bean.Reward;

@Dao("org.fireflyest.pamphlet.bean.Reward")
public interface RewardDao {
    
    @Select("SELECT * FROM `pamphlet_reward` WHERE `type`='${type}' AND `season`=${season};")
    Reward[] selectRewardByType(String type, int season);

    @Insert("INSERT INTO `pamphlet_reward` (`type`,`season`,`num`,`item`) VALUES ('${type}',${season},${num},'${item}');")
    long insertReward(String type, int season, int num, String item);

    @Update("UPDATE `pamphlet_reward` SET `commands`='${commands}' WHERE `id`=${id};")
    long updateRewardCommands(long id, String commands);

    @Update("UPDATE `pamphlet_reward` SET `name`='${name}' WHERE `id`=${id};")
    long updateRewardName(long id, String name);

}
