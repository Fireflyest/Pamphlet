package org.fireflyest.pamphlet.dao;

import java.sql.*;
import java.util.*;

public class SteveDaoImpl implements SteveDao {

    private final String url;

    private static final String createTable = "CREATE TABLE IF NOT EXISTS `pamphlet_steve`(  `uid` ${java.lang.String} NOT NULL PRIMARY KEY,  `series` ${int} DEFAULT '0',  `name` ${java.lang.String} DEFAULT NULL,  `season` ${int} DEFAULT '0',  `signed` ${int} DEFAULT '0',  `exp` ${int} DEFAULT '0',  `gain` ${int} DEFAULT '0');";

    public java.lang.String getCreateTableSQL(){ return createTable; }

    /**
     * 自动生成的数据访问层
     * @param url 链接
     */
    public SteveDaoImpl(String url) {
        this.url = url;
    }

    @Override
    public org.fireflyest.pamphlet.bean.Steve selectSteveByUid(java.lang.String uid) {
        String sql = "SELECT * FROM `pamphlet_steve` WHERE `uid`='" + uid.replace("'", "''") + "';";
        org.fireflyest.pamphlet.bean.Steve returnValue = null;
        List<org.fireflyest.pamphlet.bean.Steve> objList = new ArrayList<>();
        
        Connection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                org.fireflyest.pamphlet.bean.Steve obj = new org.fireflyest.pamphlet.bean.Steve();
                obj.setUid(resultSet.getString("uid"));
                obj.setSeries(resultSet.getInt("series"));
                obj.setName(resultSet.getString("name"));
                obj.setSeason(resultSet.getInt("season"));
                obj.setSigned(resultSet.getInt("signed"));
                obj.setExp(resultSet.getInt("exp"));
                obj.setGain(resultSet.getInt("gain"));
                objList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (objList.size() != 0) {
            returnValue = objList.get(0);
        }
        return returnValue;
    }

    @Override
    public long insertSteve(java.lang.String uid, java.lang.String name, int season) {
        String sql = "INSERT INTO `pamphlet_steve` (`uid`,`name`,`season`) VALUES ('" + uid.replace("'", "''") + "','" + name.replace("'", "''") + "'," + season + ");";
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

}