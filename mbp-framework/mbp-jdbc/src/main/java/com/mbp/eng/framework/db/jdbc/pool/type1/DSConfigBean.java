package com.mbp.eng.framework.db.jdbc.pool.type1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ChenPei
 * 配置文件Bean类
 * 单个数据库连接信息Bean
 */
public class DSConfigBean {
    private static final Logger logger = LoggerFactory.getLogger(DSConfigBean.class);

    //数据库类型
    private String type = "";
    //连接池名字
    private String name = "";
    //数据库驱动
    private String driver = "";
    //数据库url
    private String url = "";
    //用户名
    private String username = "";
    //密码
    private String password = "";
    //最大连接数
    private int maxconn = 0;

    /**
     *
     */
    public DSConfigBean() {
        // TODO Auto-generated constructor stub
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
     * @return the maxconn
     */
    public int getMaxconn() {
        return maxconn;
    }

    /**
     * @param maxconn the maxconn to set
     */
    public void setMaxconn(int maxconn) {
        this.maxconn = maxconn;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}