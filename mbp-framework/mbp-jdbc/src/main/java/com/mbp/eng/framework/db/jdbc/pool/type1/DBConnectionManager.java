package com.mbp.eng.framework.db.jdbc.pool.type1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author ChenPei
 * 数据库管理类
 */
public class DBConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(DBConnectionManager.class);

    // 客户连接数
    private static int clients;
    // 驱动信息
    private Vector vector = new Vector();
    // 连接池
    private Hashtable hashtable = new Hashtable();

    /***********************↓↓↓↓↓↓单例(双重校验锁)↓↓↓↓↓↓↓*****************/
    // 唯一数据库连接池管理实例类
    private static volatile DBConnectionManager instance;

    /**
     * 实例化管理类
     */
    private DBConnectionManager() {
        long time = System.currentTimeMillis();
        //logger.info("DBConnectionManager.DBConnectionManager time:{}", time);
        // TODO Auto-generated constructor stub
        this.init();
    }

    /**
     * 得到唯一实例管理类
     *
     * @return
     */
    public static DBConnectionManager getInstance() {
        if (instance == null) {
            synchronized (DBConnectionManager.class) {
                if (instance == null)
                    //instance为 volatile,现在没问题了
                    instance = new DBConnectionManager();
            }
        }
        return instance;
    }
    /***********************↑↑↑↑↑↑单例(双重校验锁)↑↑↑↑↑↑↑*****************/
    /**
     * 释放连接
     *
     * @param name
     * @param connection
     */
    public void freeConnection(String name, Connection connection) {
        // 根据关键名字得到连接池
        DBConnectionPool dbConnectionPool = (DBConnectionPool) hashtable.get(name);
        if (dbConnectionPool != null)
            // 释放连接
            dbConnectionPool.freeConnection(connection);
    }

    /**
     * 得到一个连接根据连接池的名字name
     *
     * @param name
     * @return
     */
    public Connection getConnection(String name) {
        DBConnectionPool dbConnectionPool = null;
        Connection connection = null;
        // 从名字中获取连接池
        dbConnectionPool = (DBConnectionPool) hashtable.get(name);
        // 从选定的连接池中获得连接
        connection = dbConnectionPool.getConnection();
        if (connection != null)
            System.out.println("得到连接...");
        return connection;
    }

    /**
     * 得到一个连接,根据连接池的名字和等待时间
     *
     * @param name
     * @param timeout
     * @return
     */
    public Connection getConnection(String name, long timeout) {
        DBConnectionPool dbConnectionPool = null;
        Connection connection = null;
        // 从名字中获取连接池
        dbConnectionPool = (DBConnectionPool) hashtable.get(name);
        // 从选定的连接池中获得连接
        connection = dbConnectionPool.getConnection(timeout);
        System.out.println("得到连接...");
        return connection;
    }

    /**
     * 释放所有连接
     */
    public synchronized void release() {
        Enumeration enumeration = hashtable.elements();
        while (enumeration.hasMoreElements()) {
            DBConnectionPool dbConnectionPool = (DBConnectionPool) enumeration.nextElement();
            if (dbConnectionPool != null)
                dbConnectionPool.release();
        }
        hashtable.clear();
    }

    /**
     * 创建连接池
     *
     * @param dsConfigBean
     */
    private void createPools(DSConfigBean dsConfigBean) {
        DBConnectionPool dbConnectionPool = new DBConnectionPool();
        dbConnectionPool.setName(dsConfigBean.getName());
        dbConnectionPool.setDriver(dsConfigBean.getDriver());
        dbConnectionPool.setUrl(dsConfigBean.getUrl());
        dbConnectionPool.setUser(dsConfigBean.getUsername());
        dbConnectionPool.setPassword(dsConfigBean.getPassword());
        dbConnectionPool.setMaxConn(dsConfigBean.getMaxconn());
        //最大连接数(maxconn)
        //System.out.println("ioio:" + dsConfigBean.getMaxconn());
        hashtable.put(dsConfigBean.getName(), dbConnectionPool);
    }

    /**
     * 初始化连接池的参数
     */
    private void init() {
        // 加载驱动程序
        this.loadDrivers();
        // 创建连接池
        Iterator alldriver = vector.iterator();
        while (alldriver.hasNext()) {
            this.createPools((DSConfigBean) alldriver.next());
            //System.out.println("创建连接池...");
        }
        System.out.println("创建连接池完毕...");
    }

    /**
     * 加载驱动程序
     */
    private void loadDrivers() {
        ParseDSConfig parseDSConfig = new ParseDSConfig();
        // 读取数据库配置文件
        vector = parseDSConfig.readConfigInfo("data-source.xml");
        System.out.println("加载驱动程序...");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DBConnectionManager.getInstance().getConnection("hive");
    }
}