package com.mbp.eng.framework.clickhouse.jdbc.conn.type1.test;

import com.mbp.eng.framework.clickhouse.jdbc.conn.type1.ClickHouseConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickHouseConnectTest {
    private static Logger logger = LoggerFactory.getLogger(ClickHouseConnectTest.class);
    /**
     * 创建表
     *
     * @param createTableSQL
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void createTable(String createTableSQL) throws SQLException, ClassNotFoundException {
        //logger.info("ClickHouseClient.createTable driver:{} url:{} userName:{} password:{} createTableSQL{}", driver, url, userName, password, createTableSQL);
        Connection connection = ClickHouseConnect.getInstance().getConnection();

        /*PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL);
        int a = preparedStatement.executeUpdate();*/
        Statement statement = connection.createStatement();
        statement.executeUpdate(createTableSQL);

        // 完成后关闭
        connection.close();
    }

    /**
     * 删除表
     *
     * @param clickhouseTable
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void dropTable(String clickhouseTable) throws SQLException, ClassNotFoundException {
        //logger.info("ClickHouseClient.dropTable driver:{} url:{} userName:{} password:{} clickhouseTable{}", driver, url, userName, password, clickhouseTable);
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //开启事务
        //connection.setAutoCommit(false);
        String dropTableSQL = "drop table " + clickhouseTable;
        /*PreparedStatement preparedStatement = connection.prepareStatement(dropTableSQL);
        preparedStatement.executeUpdate();*/
        Statement statement = connection.createStatement();
        statement.executeUpdate(dropTableSQL);
        //提交事务
        connection.commit();
        // 完成后关闭
        connection.close();
    }

    /**
     * 清空表数据
     *
     * @param clickhouseTable
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void truncateTable(String clickhouseTable) throws SQLException, ClassNotFoundException {
        //logger.info("ClickHouseClient.truncateTable driver:{} url:{} userName:{} password:{} clickhouseTable{}", driver, url, userName, password, clickhouseTable);
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //开启事务
        //connection.setAutoCommit(false);
        String truncateSQL = "truncate table " + clickhouseTable;
        /*PreparedStatement preparedStatement = connection.prepareStatement(truncateSQL);
        preparedStatement.executeUpdate();*/
        Statement statement = connection.createStatement();
        statement.executeUpdate(truncateSQL);
        //提交事务
        connection.commit();
        // 完成后关闭
        connection.close();
    }

    /**
     * 重命名
     *
     * @param clickhouseTable
     * @param clickhouseTableNew
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void renameTable(String clickhouseTable, String clickhouseTableNew) throws SQLException, ClassNotFoundException {
        //logger.info("ClickHouseClient.renameTable driver:{} url:{} userName:{} password:{} clickhouseTable{} clickhouseTableNew{}", driver, url, userName, password, clickhouseTable, clickhouseTableNew);
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //开启事务
        //connection.setAutoCommit(false);
        String renameSQL = "rename table " + clickhouseTable + " to " + clickhouseTableNew;
        /*PreparedStatement preparedStatement = connection.prepareStatement(renameSQL);
        preparedStatement.executeUpdate();*/
        Statement statement = connection.createStatement();
        statement.executeUpdate(renameSQL);
        //提交事务
        connection.commit();
        //完成后关闭
        connection.close();
    }

    /**
     * 删除分区
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void dropPartition(String dropPartitionSQL) throws SQLException, ClassNotFoundException {
        //logger.info("ClickHouseClient.dropPartition driver:{} url:{} userName:{} password:{}", driver, url, userName, password);
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //开启事务
        //connection.setAutoCommit(false);
        /*PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
        preparedStatement.executeUpdate();*/
        Statement statement = connection.createStatement();
        statement.executeUpdate(dropPartitionSQL);
        //完成后关闭
        connection.close();
    }

    /**
     * 增加数据
     *
     * @param insertSQL
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void insert(String insertSQL) throws SQLException, ClassNotFoundException {
        //logger.info("ClickHouseClient.add driver:{} url:{} userName:{} password:{}", driver, url, userName, password);
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //开启事务
        //connection.setAutoCommit(false);
        /*PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.executeUpdate();*/
        Statement statement = connection.createStatement();
        statement.executeUpdate(insertSQL);
        //提交事务
        connection.commit();
        //完成后关闭
        connection.close();
    }
    /**
     * 增加数据
     *
     * @param insertSQL
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void addBitMapData(String insertSQL) throws SQLException, ClassNotFoundException {
        //logger.info("ClickHouseClient.add driver:{} url:{} userName:{} password:{}", driver, url, userName, password);
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //开启事务
        //connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.executeUpdate();
        /*Statement statement = connection.createStatement();
        statement.executeUpdate(insertSQL);*/
        //提交事务
        connection.commit();
        //完成后关闭
        connection.close();
    }

    /**
     * 删除数据
     */
    public static void delete(String tableName) throws SQLException, ClassNotFoundException {
        //logger.info("ClickHouseClient.delete driver:{} url:{} userName:{} password:{}", driver, url, userName, password);
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //开启事务
        //connection.setAutoCommit(false);
        String deleteTableSQL = "delete from " + tableName;
        /*PreparedStatement preparedStatement = connection.prepareStatement(deleteTableSQL);
        preparedStatement.executeUpdate();*/
        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteTableSQL);
        //完成后关闭
        connection.close();
    }

    /**
     * 修改数据
     *
     * @param updateSQL
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void update(String updateSQL) throws SQLException, ClassNotFoundException {
        //logger.info("ClickHouseClient.update driver:{} url:{} userName:{} password:{}", driver, url, userName, password);
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //开启事务
        //connection.setAutoCommit(false);
        /*PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
        preparedStatement.executeUpdate();*/
        Statement statement = connection.createStatement();
        statement.executeUpdate(updateSQL);
        //完成后关闭
        connection.close();
    }

    public static int getRowCount(String querySQL) throws SQLException, ClassNotFoundException {
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        int rowCount = 0;
        String sql = "select count(*) record_ from(" + querySQL + ")";
        //logger.info("ClickHouseClient.getRowCount sql:{}", sql);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //执行查询语句并返回结果集
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            rowCount = resultSet.getInt("record_");
        }
        resultSet.close();
        preparedStatement.close();
        //完成后关闭
        connection.close();
        return rowCount;
    }

    public static List<Map<String, Object>> searchSQL(String querySQL) throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> searchSQL = searchSQL(querySQL, null);
        return searchSQL;
    }

    /**
     * 根据connection和查询SQL把结果集返回到List<Map < String, Object>>
     *
     * @param querySQL
     * @param param
     * @return
     * @throws SQLException
     */
    protected static List<Map<String, Object>> searchSQL(String querySQL, Object... param) throws SQLException, ClassNotFoundException {
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //logger.info("ClickHouseClient.searchSQL sql:{}", querySQL);
        PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
        //给参数赋值
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                preparedStatement.setObject(i, param[i]);
            }
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount = resultSetMetaData.getColumnCount();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            Object[] rsStr = new Object[colCount];
            for (int i = 0; i < colCount; i++) {
                // 获得指定列的列名
                String columnName = resultSetMetaData.getColumnName(i + 1);
                if (columnName.lastIndexOf(".") > -1) {
                    columnName = columnName.substring(columnName.lastIndexOf(".") + 1, columnName.length());
                }
                rsStr[i] = resultSet.getObject(i + 1);
                map.put(columnName, rsStr[i]);
            }
            list.add(map);
        }
        resultSet.close();
        preparedStatement.close();
        //完成后关闭
        connection.close();
        return list;
    }

    /**
     * 查询数据
     *
     * @param querySQL
     * @param className
     * @param <T>
     * @return list
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> query(String querySQL, Class<T> className) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //logger.info("ClickHouseClient.query driver:{} url:{} userName:{} password:{}", driver, url, userName, password);
        Connection connection = ClickHouseConnect.getInstance().getConnection();
        //开启事务
        //connection.setAutoCommit(false);
        /*PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
        //执行查询语句并返回结果集
        ResultSet resultSet = preparedStatement.executeQuery();*/

        Statement statement = connection.createStatement();
        //执行查询语句并返回结果集
        ResultSet resultSet = statement.executeQuery(querySQL);
        List<T> list = new ArrayList<T>();
        Field[] fields = className.getDeclaredFields();
        while (resultSet.next()) {
            T instance = className.newInstance();
            for (Field field : fields) {
                Object result = resultSet.getObject(field.getName());
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                field.set(instance, result);
                field.setAccessible(flag);
            }
            list.add(instance);
        }
        //提交事务
        connection.commit();
        //完成后关闭
        connection.close();
        return list;
    }
}
