package com.mbp.eng.framework.mysql.jdbc.conn.type1.test;

import com.mbp.eng.framework.mysql.jdbc.conn.type1.MySQLConn;
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

public class MySQLConnTest {
    private static Logger logger = LoggerFactory.getLogger(MySQLConnTest.class);

    public static List<Map<String, Object>> searchSQL(Connection connection, String querySQL) throws SQLException {
        List<Map<String, Object>> searchSQL = searchSQL(connection, querySQL, null);
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
    protected static List<Map<String, Object>> searchSQL(Connection connection, String querySQL, Object... param) throws SQLException {
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
    public static <T> List<T> query(Connection connection, String querySQL, Class<T> className) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
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

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String driver = "com.mysql.jdbc.Driver";
        String dbUrl = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "root123ABC";

        Connection connection = MySQLConn.getInstance().getMySQLConnection(driver, dbUrl, user, password);
        String sqlStr = "select * from scheduler_task_info where id=1";
        List<Map<String, Object>> sqlList = MySQLConnTest.searchSQL(connection, sqlStr);

        for (int i = 0; i < sqlList.size(); i++) {
            System.out.println("" + sqlList.get(i).get("audienceId"));
        }
    }
}
