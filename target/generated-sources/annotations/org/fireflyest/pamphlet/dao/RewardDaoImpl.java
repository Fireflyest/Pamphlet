package org.fireflyest.pamphlet.dao;

import java.sql.*;
import java.util.*;

public class RewardDaoImpl implements RewardDao {

    private final String url;

    private static final String createTable = "CREATE TABLE IF NOT EXISTS `pamphlet_reward`(  `item` text DEFAULT NULL,  `num` ${int} DEFAULT NULL,  `name` ${java.lang.String} DEFAULT NULL,  `season` ${int} DEFAULT NULL,  `id` integer NOT NULL PRIMARY KEY AUTO_INCREMENT,  `type` ${java.lang.String} DEFAULT NULL,  `commands` ${java.lang.String} DEFAULT NULL);";

    public java.lang.String getCreateTableSQL(){ return createTable; }

    /**
     * 自动生成的数据访问层
     * @param url 链接
     */
    public RewardDaoImpl(String url) {
        this.url = url;
    }

    @Override
    public org.fireflyest.pamphlet.bean.Reward[] selectRewardByType(java.lang.String type, int season) {
        String sql = "SELECT * FROM `pamphlet_reward` WHERE `type`='" + type.replace("'", "''") + "' AND `season`=" + season + ";";
        org.fireflyest.pamphlet.bean.Reward[] returnValue;
        List<org.fireflyest.pamphlet.bean.Reward> objList = new ArrayList<>();
        
        Connection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                org.fireflyest.pamphlet.bean.Reward obj = new org.fireflyest.pamphlet.bean.Reward();
                obj.setItem(resultSet.getString("item"));
                obj.setNum(resultSet.getInt("num"));
                obj.setName(resultSet.getString("name"));
                obj.setSeason(resultSet.getInt("season"));
                obj.setId(resultSet.getLong("id"));
                obj.setType(resultSet.getString("type"));
                obj.setCommands(resultSet.getString("commands"));
                objList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        returnValue = objList.toArray(new org.fireflyest.pamphlet.bean.Reward[0]);
        return returnValue;
    }

    @Override
    public long insertReward(java.lang.String type, int season, int num, java.lang.String item) {
        String sql = "INSERT INTO `pamphlet_reward` (`type`,`season`,`num`,`item`) VALUES ('" + type.replace("'", "''") + "'," + season + "," + num + ",'" + item.replace("'", "''") + "');";
        long insertId = 0;
        Connection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                insertId = resultSet.getInt(1);
            }
            return insertId;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                  e.printStackTrace();
                }
            }
        }
        return insertId;
    }

    @Override
    public long updateRewardCommands(long id, java.lang.String commands) {
        String sql = "UPDATE `pamphlet_reward` SET `commands`='" + commands.replace("'", "''") + "' WHERE `id`=" + id + ";";
        long num = 0;
        Connection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            num = preparedStatement.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    @Override
    public long updateRewardName(long id, java.lang.String name) {
        String sql = "UPDATE `pamphlet_reward` SET `name`='" + name.replace("'", "''") + "' WHERE `id`=" + id + ";";
        long num = 0;
        Connection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            num = preparedStatement.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

}