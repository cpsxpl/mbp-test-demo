package com.mbp.eng.framework.jdbc.sql.table;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class Log {
    private static Properties prop = null;

    private static String propertiesFile = "config/log.properties";

    private static String logFile = "log/log.txt";

    private static long mLastModifiedTime = 0L;

    private static File file = null;

    private static PrintWriter printWriter;

    static {
        init();
    }

    public Log() {
        init();
    }

    public static void main(String[] args) {
        Log log1 = new Log();
    }

    private static void init() {
        file = new File(propertiesFile);

        mLastModifiedTime = file.lastModified();
        if (mLastModifiedTime == 0L) {
            System.out.println("the File :" + propertiesFile
                    + " does not exist!");
        }
        try {
            FileInputStream is = new FileInputStream(file);
            prop = new Properties();
            prop.load(is);
            prop.list(System.out);
            is.close();

            printWriter = new PrintWriter(new FileWriter(logFile, true), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperties(String key, String defaultValue) {
        long newTime = file.lastModified();
        if (newTime == 0L) {
            if (mLastModifiedTime == 0L)
                System.out.println("the File :" + propertiesFile
                        + " does not exist!");
            else {
                System.out.println("the File :" + propertiesFile
                        + " was deleted!");
            }
            return defaultValue;
        }
        if (newTime > mLastModifiedTime) {
            prop.clear();
            try {
                FileInputStream fis = new FileInputStream(file);
                prop.load(fis);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mLastModifiedTime = newTime;
        String value = prop.getProperty(key, defaultValue);
        return value;
    }

    private static boolean isDebug() {
        String debug = getProperties("isDebug", "false");
        return debug.equals("true");
    }

    private static boolean isLog() {
        String debug = getProperties("isLog", "false");
        return debug.equals("true");
    }

    public static void Debug(Object o) {
        try {
            if (isDebug()) {
                if ((o instanceof Throwable))
                    ((Throwable) o).printStackTrace(System.out);
                else {
                    System.out.println(o);
                }
                log(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void log(Object o) {
        try {
            if (isLog()) {
                printWriter.println(new Date() + ": " + o);
                if ((o instanceof Throwable))
                    ((Throwable) o).printStackTrace(printWriter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}