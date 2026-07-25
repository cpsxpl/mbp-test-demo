package com.mbp.eng.framework.mysql.jdbc.pool.type1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * 数据库访问配置文件各参数的获取
 *
 * @author ChenPei
 */
public class DbConfig {
    private static final Logger logger = LoggerFactory.getLogger(DbConfig.class);

    // 数据库及server配置文件路径
    private static final String ACTIONPATH = "mysql.properties";

    private String url = null;
    private String driver = null;
    private String username = null;
    private String password = null;

    /***********************↓↓↓↓↓↓单例(双重校验锁)↓↓↓↓↓↓↓*****************/
    private static volatile DbConfig instance = null;

    private DbConfig() {
        long time = System.currentTimeMillis();
        logger.info("DbConfig.DbConfig time:{}", time);
    }

    public static DbConfig getInstance() {
        if (instance == null) {
            synchronized (DbConfig.class) {
                if (instance == null)
                    //instance为 volatile,现在没问题了
                    instance = new DbConfig().getDbConfig();
            }
        }
        return instance;
    }

    /***********************↑↑↑↑↑↑单例(双重校验锁)↑↑↑↑↑↑↑*****************/

    private DbConfig getDbConfig() {
        DbConfig dbConfig = new DbConfig();
        Properties properties = new Properties();
        String path = null;
        FileInputStream fileInputStream = null;
        try {
            path = DbConfig.class.getClassLoader().getResource("").toURI().getPath();
            fileInputStream = new FileInputStream(new File(path + ACTIONPATH));
            properties.load(fileInputStream);
            dbConfig.url = properties.getProperty("url");
            dbConfig.driver = properties.getProperty("driver");
            dbConfig.username = properties.getProperty("username");
            dbConfig.password = properties.getProperty("password");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dbConfig;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}