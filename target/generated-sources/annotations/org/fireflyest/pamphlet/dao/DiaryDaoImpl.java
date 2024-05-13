package org.fireflyest.pamphlet.dao;

import java.sql.*;
import java.util.*;

public class DiaryDaoImpl implements DiaryDao {

    private final String url;

    private static final String createTable = "CREATE TABLE IF NOT EXISTS `pamphlet_diary`(  `sign` ${boolean} DEFAULT '0',  `playtime` ${long} DEFAULT '0',  `target` ${java.lang.String} NOT NULL PRIMARY KEY);";

    public java.lang.String getCreateTableSQL(){ return createTable; }

    /**
     * 自动生成的数据访问层
     * @param url 链接
     */
    public DiaryDaoImpl(String url) {
        this.url = url;
    }

    @Override
    public org.fireflyest.pamphlet.bean.Diary selectDiaryByTarget(java.lang.String target) {
        String sql = "SELECT * FROM `pamphlet_diary` WHERE `target`='" + target.replace("'", "''") + "';";
        org.fireflyest.pamphlet.bean.Diary returnValue = null;
        List<org.fireflyest.pamphlet.bean.Diary> objList = new ArrayList<>();
        
        Connection connection = org.fireflyest.craftdatabase.sql.SQLConnector.getConnect(url);
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                org.fireflyest.pamphlet.bean.Diary obj = new org.fireflyest.pamphlet.bean.Diary();
                obj.setSign(resultSet.getBoolean("sign"));
                obj.setPlaytime(resultSet.getLong("playtime"));
                obj.setTarget(resultSet.getString("target"));
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
    public long insertDiary(java.lang.String target) {
        String sql = "INSERT INTO `pamphlet_diary` (`target`) VALUES ('" + target.replace("'", "''") + "');";
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