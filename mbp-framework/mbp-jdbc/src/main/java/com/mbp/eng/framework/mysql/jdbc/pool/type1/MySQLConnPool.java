package com.mbp.eng.framework.mysql.jdbc.pool.type1;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class MySQLConnPool {
    // 定义线程本地变量,每个线程访问它都会获得不同的对象
    // 使用ThreadLocal使一个连接绑定到一个线程上
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    // 数据库名称
    private static String url = null;

    private static String driver = null;

    // 用户名
    private static String username = null;

    // 密码
    private static String password = null;

    // 为null时不使用连接池,jdbc/mysql
    private static String resourceName = null;
    private static String databaseType = "MySQL";

    private static void initParams() {
        url = DbConfig.getInstance().getUrl();
        driver = DbConfig.getInstance().getDriver();
        username = DbConfig.getInstance().getUsername();
        password = DbConfig.getInstance().getPassword();
    }

    /**
     * @return 得到一个数据库连接
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = threadLocal.get();
        if (connection == null) {
            if (null == resourceName) {
                if ("mysql".equals(databaseType.toLowerCase())) {
                    connection = getMySqlConnection();
                } else {
                    System.out.println("在 JdbcConnection.java 中数据库类型没有设置");
                    throw new SQLException("数据库类型未设置");
                }
            } else {
                connection = getConnectionByPool();
            }
            threadLocal.set(connection);
        }
        return connection;
    }

    // 获得MySql数据库连接
    private static Connection getMySqlConnection() {
        initParams();
        Connection connection = null;
        try {
            Class.forName(driver).newInstance(); // 加载驱动
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("MySql驱动没找到");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // 获取连接池连接
    private static Connection getConnectionByPool() {
        try {
            Context context = new InitialContext();
            Context subContext = (Context) context.lookup("java:comp/env");
            String dsName = "";
            dsName = resourceName;
            DataSource dataSource = (DataSource) subContext.lookup(dsName);
            // 上面两句可以合写成下边这句
            // ctx.lookup("java:comp/env/jdbc/oracle");// java:comp/env/
            // 规定：加前缀指定资源
            return dataSource.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭数据库连接
     *
     * @throws SQLException
     */
    public static void close() throws SQLException {
        Connection connection = threadLocal.get();
        connection.close();
        threadLocal.set(null);
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = MySQLConnPool.getConnection();
    }
}
