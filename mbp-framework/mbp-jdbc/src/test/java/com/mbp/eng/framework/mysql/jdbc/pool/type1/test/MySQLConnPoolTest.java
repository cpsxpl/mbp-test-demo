package com.mbp.eng.framework.mysql.jdbc.pool.type1.test;

import com.mbp.eng.framework.test.model.DemoInfo;
import com.mbp.eng.framework.mysql.jdbc.pool.type1.MySQLConnPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnPoolTest {

    private static Logger logger = LoggerFactory.getLogger(MySQLConnPoolTest.class);

    public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException {
        String querySQL = "select * from scheduler_task_info";
        query(querySQL, DemoInfo.class);
    }


    public static <T> List<T> query(String querySQL, Class<T> className) throws SQLException, IllegalAccessException, InstantiationException {
        logger.info("DBClient.update querySQL:{}", querySQL);
        Connection connection = MySQLConnPool.getConnection();
        //开启事务
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
        //执行查询语句并返回结果集
        ResultSet resultSet = preparedStatement.executeQuery();
        List<T> list = new ArrayList<T>();
        Field fields[] = className.getDeclaredFields();
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
        return list;
    }
}
