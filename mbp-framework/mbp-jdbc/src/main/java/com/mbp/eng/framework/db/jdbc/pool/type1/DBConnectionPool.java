package com.mbp.eng.framework.db.jdbc.pool.type1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

/**
 * @author ChenPei
 * 数据库连接池类
 */
public class DBConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(DBConnectionPool.class);

    private Connection connection = null;
    //使用的连接数
    private int inUsed = 0;
    //容器,空闲连接
    private ArrayList arrayList = new ArrayList();
    //最小连接数
    private int minConn;
    //最大连接
    private int maxConn;
    //连接池名字
    private String name;
    //密码
    private String password;
    //数据库连接地址
    private String url;
    //驱动
    private String driver;
    //用户名
    private String user;
    //定时
    public Timer timer;

    /**
     *
     */
    public DBConnectionPool() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 创建连接池
     *
     * @param driver
     * @param name
     * @param url
     * @param user
     * @param password
     * @param maxConn
     */
    public DBConnectionPool(String name, String driver, String url, String user, String password, int maxConn) {
        this.name = name;
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        this.maxConn = maxConn;
    }

    /**
     * 用完,释放连接
     *
     * @param connection
     */
    public synchronized void freeConnection(Connection connection) {
        // 添加到空闲连接的末尾
        this.arrayList.add(connection);
        this.inUsed--;
    }

    /**
     * timeout 根据timeout得到连接
     *
     * @param timeout
     * @return
     */
    public synchronized Connection getConnection(long timeout) {
        Connection connection = null;
        if (this.arrayList.size() > 0) {
            connection = (Connection) this.arrayList.get(0);
            if (connection == null)
                //继续获得连接
                connection = getConnection(timeout);
        } else {
            //新建连接
            connection = newConnection();
        }
        if (this.maxConn == 0 || this.maxConn < this.inUsed) {
            //达到最大连接数，暂时不能获得连接了。
            connection = null;
        }
        if (connection != null) {
            this.inUsed++;
        }
        return connection;
    }

    /**
     * 从连接池里得到连接
     *
     * @return
     */
    public synchronized Connection getConnection() {
        Connection connection = null;
        if (this.arrayList.size() > 0) {
            connection = (Connection) this.arrayList.get(0);
            //如果连接分配出去了,就从空闲连接里删除
            this.arrayList.remove(0);
            if (connection == null)
                //继续获得连接
                connection = getConnection();
        } else {
            //新建连接
            connection = newConnection();
        }
        if (this.maxConn == 0 || this.maxConn < this.inUsed) {
            //等待 超过最大连接时
            connection = null;
        }
        if (connection != null) {
            this.inUsed++;
            System.out.println("得到　" + this.name + "　的连接,现有" + inUsed + "个连接在使用!");
        }
        return connection;
    }

    /**
     * 释放全部连接
     */
    public synchronized void release() {
        Iterator iterator = this.arrayList.iterator();
        while (iterator.hasNext()) {
            Connection connection = (Connection) iterator.next();
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.arrayList.clear();
    }

    /**
     * 创建新连接
     *
     * @return
     */
    private Connection newConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("sorry can't find db driver!");
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            System.out.println("sorry can't create Connection!");
        }
        return connection;
    }

    /**
     * 定时处理函数
     */
    public synchronized void TimerEvent() {
        // 暂时还没有实现以后会加上的
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

    /**
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @param driver the driver to set
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * @return the maxConn
     */
    public int getMaxConn() {
        return maxConn;
    }

    /**
     * @param maxConn the maxConn to set
     */
    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    /**
     * @return the minConn
     */
    public int getMinConn() {
        return minConn;
    }

    /**
     * @param minConn the minConn to set
     */
    public void setMinConn(int minConn) {
        this.minConn = minConn;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    public void timerExpired(Timer timer) {
        // TODO Auto-generated method stub
    }
}