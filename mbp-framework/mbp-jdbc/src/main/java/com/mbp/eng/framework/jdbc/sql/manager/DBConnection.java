package com.mbp.eng.framework.jdbc.sql.manager;

import com.mbp.eng.framework.jdbc.sql.table.LogFile;
import com.mbp.eng.framework.jdbc.sql.table.NullConnectionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class DBConnection {
    private static final String DATA_SOURCE_PROPERTIES = "config/ejb-data-source.properties";

    private static final String PROCEDURE_JAVA = "java";

    private static boolean executeProcedureByJava = false;

    private static DataSource dataSource;

    private static Map addDataSourceMap = new HashMap();

    public static boolean isLog;

    public static boolean isDebug;

    private static LogFile logFile;

    private static String logDirectory;

    static {
        initDataSource();
    }

    private DBConnection() {
    }

    public static Properties getLogProperties() throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(DATA_SOURCE_PROPERTIES);
        properties.load(fileInputStream);
        fileInputStream.close();

        return properties;
    }

    public static String getLogDirectory() {
        return logDirectory;
    }

    public static Connection getConnection(String name) throws NullConnectionException {
        Connection conn = null;
        try {
            DataSource ds = getDataSource(name);
            conn = ds.getConnection();
            if (conn == null) {
                throw new NullConnectionException("connection is null");
            }

            return conn;
        } catch (Exception e) {
            try {
                if ((conn != null) && (!(conn.isClosed())))
                    conn.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            throw new NullConnectionException("connection is null");
        }
    }

    public static DataSource getDataSource(String name) {
        if (name == null) {
            return getDataSource();
        }

        DataSource ds = (DataSource) addDataSourceMap.get(name);
        if (ds == null) {
            return getDataSource();
        }

        return ds;
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            initDataSource();
        }

        return dataSource;
    }

    private static void initDataSource() {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("config/shinewaysoft/ejb-data-source.properties");
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String isLogStr = properties.getProperty("isLog", "false");
        String isDebugStr = properties.getProperty("isDebug", "false");

        isLog = (isLogStr != null) && (isLogStr.trim().equalsIgnoreCase("true"));

        isDebug = (isDebugStr != null) && (isDebugStr.trim().equalsIgnoreCase("true"));

        String procedure = properties.getProperty("procedure", "db");
        executeProcedureByJava = (procedure != null) && (procedure.trim().equalsIgnoreCase("java"));

        String factory = properties.getProperty("factory", "com.evermind.server.rmi.RMIInitialContextFactory");
        String url = properties.getProperty("url", "ormi://localhost:23799/sws-app");
        String user = properties.getProperty("user", "admin");
        String password = properties.getProperty("password", "shinewaysoft");
        String jndi = properties.getProperty("jndi", "jdbc/oracleDS");

        Hashtable hashtable = new Hashtable();
        hashtable.put("java.naming.factory.initial", factory);
        hashtable.put("java.naming.provider.url", url);
        hashtable.put("java.naming.security.principal", user);
        hashtable.put("java.naming.security.credentials", password);

        InitialContext initialContext = null;
        try {
            initialContext = new InitialContext(hashtable);
            dataSource = (DataSource) initialContext.lookup(jndi);
            if (isLog) {
                // try {
                String logDirectory = properties.getProperty("logDirectory", "log/jdbc/");
                logDirectory = new File(logDirectory).getAbsolutePath();
                String logFilePrefix = properties.getProperty("logFilePrefix", "jdbc_");
                String logFileExtend = properties.getProperty("logFileExtend", "log");
                String logFileSeqPattern = properties.getProperty("logFileSeqPattern", "yyyyMMddkkmmss");
                String logFileMaxSizeStr = properties.getProperty("logFileMaxSize", "10000000");
                long logFileMaxSize = Long.parseLong(logFileMaxSizeStr);

                logFile = new LogFile(logDirectory, logFilePrefix, logFileExtend, logFileSeqPattern, logFileMaxSize,
                        dataSource);

                // String logSeq =
                // new SimpleDateFormat("yyyyMMddkkmmss").format(
                // new Date());

                // String logFile =
                // logDirectory + logFilePrefix + logSeq + logFileExtend;

                // logPrint = new PrintWriter(new FileWriter(logFile));
                // dataSource.setLogWriter(logPrint);
                // } catch (SQLException sqle) {
                // sqle.printStackTrace();
                // } catch (IOException ioe) {
                // ioe.printStackTrace();
                // }
            }
        } catch (NamingException ne) {
            ne.printStackTrace();
        } finally {
            try {
                initialContext.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void debug(Object obj) {
        if (isDebug) {
            if (obj instanceof Throwable)
                ((Throwable) obj).printStackTrace();
            else {
                System.out.println(obj);
            }
        }

        log(obj);
    }

    public static void log(Object obj) {
        if ((isLog) && (logFile != null))
            logFile.write(obj);
    }

    public static Connection getConnection() throws NullConnectionException {
        Connection connection = null;
        try {
            if (dataSource == null) {
                initDataSource();
            }
            connection = dataSource.getConnection();
            if (connection == null) {
                throw new NullConnectionException("connection is null");
            }

            return connection;
        } catch (Exception e) {
            debug(e);
            try {
                if ((connection != null) && (!(connection.isClosed())))
                    connection.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            throw new NullConnectionException("connection is null");
        }
    }

    public static void main(String[] args) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        dbConnection.debug("SELECT 0 FROM dual");
    }
}
