package com.mbp.eng.framework.mysql.jdbc.conn.type1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class MySQLConn {
    private static final Logger logger = LoggerFactory.getLogger(MySQLConn.class);

    // 几个数据库变量
    Connection connection = null;

    Statement statement;

    ResultSet resultSet = null;

    PreparedStatement preparedStatement = null;

    private static int clients;

    public static boolean isLog;

    public static boolean isDebug;

    public static PrintWriter logPrint;

    /***********************↓↓↓↓↓↓单例(双重校验锁)↓↓↓↓↓↓↓*****************/
    private static volatile MySQLConn instance = null;

    private MySQLConn() {
        long time = System.currentTimeMillis();
        logger.info("MySQLJDBCConnection.MySQLJDBCConnection time:{}", time);
    }

    public static MySQLConn getInstance() {
        if (instance == null) {
            synchronized (MySQLConn.class) {
                if (instance == null)
                    //instance为 volatile,现在没问题了
                    instance = new MySQLConn();
            }
        }
        return instance;
    }
    /***********************↑↑↑↑↑↑单例(双重校验锁)↑↑↑↑↑↑↑*****************/
    /**
     * 构造方法:初始化链接
     *
     * @param driver
     * @param dbUrl
     * @param user
     * @param password
     */
    public void MySQLJdbcConnection(String driver, String dbUrl, String user, String password) {
        try {
            Class.forName(driver);
            // 与url指定的数据源建立连接
            connection = DriverManager.getConnection(dbUrl, user, password);
            // 采用Statement进行查询
            // connection = c.createStatement();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化链接
     *
     * @param driver
     * @param dbUrl
     * @param user
     * @param password
     * @return
     */
    public Connection getMySQLConnection(String driver, String dbUrl, String user, String password) {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(dbUrl, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * @param sql
     * @throws SQLException
     */
    public void insert(String sql) throws SQLException {
        executeUpdateNew(sql);
    }

    /**
     * @param sql
     * @throws SQLException
     */
    public void update(String sql) throws SQLException {
        executeUpdateNew(sql);
    }

    /**
     * @param sql
     * @throws SQLException
     */
    public void delete(String sql) throws SQLException {
        executeUpdateNew(sql);
    }

    /**
     * 执行查询
     *
     * @param sql
     * @return
     */
    public ResultSet executeQuery(String sql) {
        resultSet = null;
        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * 执行UPDATE INSERT DELETE
     *
     * @param sql
     * @return
     */
    public int executeUpdate(String sql) {
        int executeUpdate = 0;
        try {
            executeUpdate = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return executeUpdate;
    }

    /**
     * 执行UPDATE INSERT DELETE
     *
     * @param sql
     */
    protected void executeUpdateNew(String sql) {
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

    /**
     * 关闭链接
     */
    public void close() {
        try {
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param properties
     */
    public static void initGC(Properties properties) {
        String value = properties.getProperty("garbage-collection", "false");

        if ((value == null) || ((!(value.trim().equalsIgnoreCase("true"))) && (!(value.trim().equalsIgnoreCase("yes")))))
            return;
        value = properties.getProperty("gc-delay-time", "300000");

        long delayTime = 600000L;
        try {
            delayTime = Long.parseLong(value);
        } catch (Exception localException) {
        }
        GarbageCollector.startGCThread(delayTime);
    }

    /**
     * @param object
     */
    public static void debug(Object object) {
        if (isDebug) {
            if ((object instanceof Throwable))
                ((Throwable) object).printStackTrace();
            else {
                System.out.println(object);
            }
        }
        log(object);
    }

    /**
     * @param object
     */
    public static void log(Object object) {
        if ((isLog) && (logPrint != null)) {
            logPrint.println(SimpleDateFormat.getDateTimeInstance().format(new Date()) + ": " + object);
            if ((object instanceof Throwable)) {
                ((Throwable) object).printStackTrace(logPrint);
            }
            logPrint.flush();
        }
    }
    private static final class GarbageCollector implements Runnable {
        private long delayTime = 60000L;

        private static final long MEMORY_UNIT = 1048576L;

        private static Runtime runtime = Runtime.getRuntime();

        private static DateFormat dateFormate = SimpleDateFormat.getDateTimeInstance();

        private GarbageCollector(long time) {
            this.delayTime = time;
        }

        /**
         * @param delayTime
         */
        public static void startGCThread(long delayTime) {
            Thread thread = new Thread(new GarbageCollector(delayTime), "Garbage Collector");
            thread.setDaemon(true);
            thread.setPriority(1);
            thread.start();
        }

        /**
         * @preserve
         * @author Chen Pei
         * @Date: 2016-05-18
         */
        private static void printMemoryInfo() {
            long totalMemory = runtime.totalMemory() / 1048576L;
            long freeMemory = runtime.freeMemory() / 1048576L;
            long usedMemory = totalMemory - freeMemory;

            debug(dateFormate.format(new Date()) + " Total Memory: " + totalMemory + "M");
            debug(dateFormate.format(new Date()) + " Used Memory: " + usedMemory + "M");
            debug(dateFormate.format(new Date()) + " Free Memory: " + freeMemory + "M");
        }
        private static void performGC() {
            debug(dateFormate.format(new Date()) + " Garbage Collect Start......");
            runtime.gc();
            debug(dateFormate.format(new Date()) + " Garbage Collect End......");
        }

        public void run() {
            while (true) {
                debug("**************************************");
                printMemoryInfo();
                performGC();
                printMemoryInfo();
                debug("**************************************");
                try {
                    Thread.sleep(this.delayTime);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
