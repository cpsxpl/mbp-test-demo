package com.mbp.eng.framework.hive.jdbc.pool.type1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Vector;

public class HiveJdbcPool implements DataSource {
    private static final Logger logger = LoggerFactory.getLogger(HiveJdbcPool.class);

    /**
     * @Field: Connections
     * 使用Vector来存放数据库链接,
     * Vector具备线程安全性
     */
    private static final Vector vector = new Vector();

    static {
        //在静态代码块中加载db.properties数据库配置文件
        try {
            //数据库连接池的初始化连接数大小
            int jdbcPoolInitSize = Integer.parseInt(HiveJdbcPer.jdbcPoolInitSize);
            StringBuilder urlStringBuilder = new StringBuilder();
            //加载数据库驱动
            Class.forName(HiveJdbcPer.dbDriver);
            urlStringBuilder.append("jdbc:hive2://").append(HiveJdbcPer.dbIP).append(":").append(HiveJdbcPer.dbPort);
            if (HiveJdbcPer.iskrb5.equalsIgnoreCase("true")) {
                urlStringBuilder.append("/;principal=").append(HiveJdbcPer.krb5Principal);
            }
            String dbURL = urlStringBuilder.toString();
            for (int i = 0; i < jdbcPoolInitSize; i++) {
                Connection connection = DriverManager.getConnection(dbURL, HiveJdbcPer.dbUsername, HiveJdbcPer.dbPassword);
                logger.debug("获取到了链接：{}", connection);
                //将获取到的数据库连接加入到Connections集合中,Connections此时就是一个存放了数据库连接的连接池
                vector.addElement(connection);
            }
        } catch (SQLException e) {
            logger.error(" 创建数据库连接失败!", e.getMessage());
            try {
                throw new SQLException(" 创建数据库连接失败！");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Connection getConnection() throws SQLException {
        synchronized (vector) {
            //如果数据库连接池中的连接对象的个数大于0
            if (vector.size() > 0) {
                final Connection connection = (Connection) vector.remove(0);
                //注意需要添加OracleConnection.class,不然JdbcUtil.createOracleLob创建Clob会报错无法转换
                return (Connection) Proxy.newProxyInstance(HiveJdbcPool.class.getClassLoader(), new Class[]{Connection.class}, (proxy, method, args) -> {
                    if (!method.getName().equals("close")) {
                        return method.invoke(connection, args);
                    } else {
                        //如果调用的是Connection对象的close方法,就把conn还给数据库连接池
                        vector.addElement(connection);
                        logger.debug(connection + "被还给Connections数据库连接池了！！");
                        logger.debug("Connections数据库连接池大小为" + vector.size());
                        return null;
                    }
                });
            } else throw new RuntimeException("对不起,数据库连接失败！");
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }
    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
    }
    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }
    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
