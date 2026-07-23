package com.mbp.eng.framework.common.util.file;

import com.mbp.eng.framework.common.utils.IPUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    private Properties properties;

    public Configuration(String fileName, boolean xml) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = Configuration.class.getClassLoader();
            }
            FileInputStream fileInputStream = null;
            Properties properties = new Properties();
            if (xml) {
                fileInputStream = new FileInputStream(new File(classLoader.getResource(fileName).getPath()));
                properties.loadFromXML(fileInputStream);
            } else {
                fileInputStream = new FileInputStream(new File(fileName));
                properties.load(fileInputStream);
            }
            this.properties = properties;

            //replace $replace_local_ip in conf
            //2017-08-03 barney
            Enumeration enumeration = this.properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String propName = enumeration.nextElement().toString();
                String value = this.properties.getProperty(propName);
                if (value != null && value.contains("$replace_local_ip")) {
                    value = value.replaceAll("\\$replace_local_ip", IPUtil.getIP());
                    this.properties.setProperty(propName, value);
                }
            }

            fileInputStream.close();
        } catch (Exception e) {
            logger.warn(String.valueOf(e));
            System.out.println("yy--" + e);
        }
    }

    public Configuration(String fileName) {
        this(fileName, true);
    }

    public Configuration(Properties properties) {
        this.properties = new Properties();
    } // for test

    public Object get(Object key) {
        return this.properties.get(key);
    }

    public void set(String key, String value) {
        this.properties.setProperty(key, value);
    }

    public boolean containsKey(String key) {
        return this.properties.containsKey(key);
    }

    public List<String> getValues(Object key) {
        String value = (String) this.properties.get(key);
        String[] values = value.split(",");
        return Arrays.asList(values);
    }

    public String getDefault(String key, String defaultValue) {
        try {
            return this.properties.getProperty(key, defaultValue);
        } catch (Exception e) {
            return null;
        }
    }

    public int getInt(String name, int defaultValue) {
        String valueString = getDefault(name, "");
        if (StringUtils.isBlank(valueString))
            return defaultValue;
        try {
            return Integer.parseInt(valueString);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public long getLong(String name, long defaultValue) {
        String valueString = getDefault(name, "");
        if (StringUtils.isBlank(valueString))
            return defaultValue;
        try {
            return Long.parseLong(valueString);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public float getFloat(String name, float defaultValue) {
        String valueString = getDefault(name, "");
        if (StringUtils.isBlank(valueString))
            return defaultValue;
        try {
            return Float.parseFloat(valueString);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void addResource(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));
            Properties pro = this.properties;
            if (pro == null) {
                pro = new Properties();
                pro.loadFromXML(fileInputStream);
                fileInputStream.close();
            }
            this.properties = pro;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addResource(FileInputStream in) {
        try {
            Properties properties = this.properties;
            if (properties == null) {
                properties = new Properties();
                properties.loadFromXML(in);
                in.close();
            }
            this.properties = properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addResource(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Properties properties = this.properties;
            if (properties == null) {
                properties = new Properties();
                properties.loadFromXML(fileInputStream);
                fileInputStream.close();
            }
            this.properties = properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
