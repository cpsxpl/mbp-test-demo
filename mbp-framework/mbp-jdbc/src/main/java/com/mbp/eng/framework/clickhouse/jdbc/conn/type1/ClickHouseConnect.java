package com.mbp.eng.framework.clickhouse.jdbc.conn.type1;

import com.mbp.eng.framework.common.util.file.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.clickhouse.util.apache.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ClickHouseConnect {
    private static final Logger logger = LoggerFactory.getLogger(ClickHouseConnect.class);
    private static Properties propertiesClickhouse = PropertiesUtil.readProperties("clickhouse.properties");
    private static String clickhouseDriver = propertiesClickhouse.getProperty("clickhouse.driver");
    private static String clickhouseURL = propertiesClickhouse.getProperty("clickhouse.url");
    private static String clickhouseUserName = propertiesClickhouse.getProperty("clickhouse.username");
    private static String clickhousePassword = propertiesClickhouse.getProperty("clickhouse.password");

    private static Connection connection;
    /***********************↓↓↓↓↓↓单例(双重校验锁)↓↓↓↓↓↓↓*****************/
    private static volatile ClickHouseConnect instance;

    private ClickHouseConnect() {
        long time = System.currentTimeMillis();
        //logger.info("ClickHouseConnect time:{}", time);
    }

    public static ClickHouseConnect getInstance() {
        if (instance == null) {
            synchronized (ClickHouseConnect.class) {
                if (instance == null)
                    //instance为 volatile,现在没问题了
                    instance = new ClickHouseConnect();
            }
        }
        return instance;
    }

    /***********************↑↑↑↑↑↑单例(双重校验锁)↑↑↑↑↑↑↑*****************/

    public static Connection getConnection(String driver, String url, String userName, String password) throws ClassNotFoundException, SQLException {
        //logger.info("ClickHouseConnect.getConnection driver:{} url:{} userName:{} password:{}", driver, url, userName, password);
        Class.forName(driver);
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            connection = DriverManager.getConnection(url);
        } else {
            connection = DriverManager.getConnection(url, userName, password);
        }
        return connection;
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(clickhouseDriver);
        if (StringUtils.isBlank(clickhouseUserName) || StringUtils.isBlank(clickhousePassword)) {
            connection = DriverManager.getConnection(clickhouseURL);
        } else {
            connection = DriverManager.getConnection(clickhouseURL, clickhouseUserName, clickhousePassword);
        }
        return connection;
    }
}
