package com.mbp.eng.framework.hive.jdbc.pool.type1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcUtil {
    private static final Logger logger = LoggerFactory.getLogger(HiveJdbcUtil.class);
    /**
     * 数据库连接池
     */
    private static HiveJdbcPool hiveJdbcPool = new HiveJdbcPool();
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = hiveJdbcPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Hive JDBC 获取连接池中连接异常！");
        }
        return connection;
    }

    public static ResultSet executeQuery(Connection connection, String sqlstring) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlstring);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * 释放资源，
     * 释放的资源包括Connection数据库连接对象，负责执行SQL命令的Statement对象，存储查询结果的ResultSet对象
     *
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void release(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                logger.debug("关闭存储查询结果的ResultSet对象");
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                logger.debug("关闭负责执行SQL命令的Statement对象");
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                logger.debug("关闭Connection数据库连接对象");
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
