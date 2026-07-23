package com.mbp.eng.framework.common.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Properties readProperties(String filename) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            //文件:/conf.properties
            //inputStream = PropertiesUtil.class.getResourceAsStream(filename);
            //文件:conf.properties
            inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(filename);
            if (inputStream == null) {
                logger.error("Sorry, unable to find " + filename);
                return null;
            }
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readValueFromProperties(String filename, String keyName) {
        Properties properties = readProperties(filename);
        return properties.getProperty(keyName);
    }
}
