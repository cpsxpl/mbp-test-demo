package com.mbp.eng.framework.hive.jdbc.pool.type1;

import com.mbp.eng.framework.common.util.file.PropertiesUtil;

import java.util.Properties;

public class HiveJdbcPer {
    /**
     * hive-jdbc.properties配置文件Properties
     */
    public static Properties properties = PropertiesUtil.readProperties("hive-jdbc.properties");

    /**
     * 初始化连接池数
     */
    static String jdbcPoolInitSize = properties.getProperty("db.jdbcPoolInitSize");

    /**
     * Hive JDBC Driver类
     */
    static String dbDriver = properties.getProperty("db.driver");

    /**
     * hiveserver2 的ip地址
     */
    static String dbIP = properties.getProperty("db.ip");

    /**
     * hiveserver2 的端口
     */
    static String dbPort = properties.getProperty("db.port");

    /**
     * hiveserver2连接数据库名称
     */
    public static String dbName = properties.getProperty("db.name");
    /**
     * hiveserver2连接用名名称
     */
    static final String dbUsername = properties.getProperty("db.username");

    /**
     * hiveserver2连接用名密码
     */
    static final String dbPassword = properties.getProperty("db.password");

    /**
     * hiveserver2连接是否开启Kerberos认证
     */
    static final String iskrb5 = properties.getProperty("db.iskrb5");

    /**
     * hiveserver2开启Kerberos认证主体
     */
    static final String krb5Principal = properties.getProperty("hive.principal");

    /**
     * Hive集群上的HDFS存储目录
     */
    static String storageDirectory = properties.getProperty("storage.directory");
}
