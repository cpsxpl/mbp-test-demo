package com.mbp.eng.framework.common.utils;

import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import com.typesafe.config.ConfigValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class ConfigUtils {
    private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    public static final String JAVA_HOME = "JAVA_HOME";
    public static final String HADOOP_HOME = "HADOOP_HOME";
    public static final String SPARK_HOME = "SPARK_HOME";

    public static Config configFromPath(String path) {
        File configFile = new File(path);
        return ConfigFactory.parseFile(configFile);
    }

    public static Config configFromResource(String resource) {
        try (Reader reader = new InputStreamReader(ConfigUtils.class.getClassLoader().getResourceAsStream(resource))) {
            return ConfigFactory.parseReader(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config findReplaceStringValues(Config config, String findRegex, Object replace) {
        for (Map.Entry<String, ConfigValue> valueEntry : config.entrySet()) {
            ConfigValueType valueType = valueEntry.getValue().valueType();
            if (valueType.equals(ConfigValueType.OBJECT)) {
                config = ConfigUtils.findReplaceStringValues(config.getConfig(valueEntry.getKey()), findRegex, replace);
            } else if (valueType.equals(ConfigValueType.LIST)) {
                @SuppressWarnings("unchecked")
                List<Object> valueList = (List<Object>) valueEntry.getValue().unwrapped();
                if (valueList.size() > 0) {
                    if (valueList.get(0) instanceof String) {
                        for (int i = 0; i < valueList.size(); i++) {
                            String found = (String) valueList.get(0);
                            String replaced = found.replaceAll(findRegex, replace.toString());
                            valueList.set(i, replaced);
                        }
                    }
                }
                config = config.withValue(valueEntry.getKey(), ConfigValueFactory.fromAnyRef(valueList));
            } else if (valueType.equals(ConfigValueType.STRING)) {
                String found = (String) valueEntry.getValue().unwrapped();
                String replaced = found.replaceAll(findRegex, replace.toString());
                config = config.withValue(valueEntry.getKey(), ConfigValueFactory.fromAnyRef(replaced));
            }
        }

        return config;
    }

    public static <T> T getOrElse(Config config, String path, T orElse) {
        if (config.hasPath(path)) {
            if (config.getValue(path).valueType() == ConfigValueType.OBJECT) {
                return (T) config.getConfig(path);
            } else {
                return (T) config.getAnyRef(path);
            }
        } else {
            return orElse;
        }
    }

    public static Map<String, String> getEnv(Config config) {
        Map<String, String> env = Maps.newHashMap();
        env.put("JAVA_HOME", ConfigUtils.getOrElse(config, JAVA_HOME, System.getenv("JAVA_HOME")));
        env.put("HADOOP_HOME", ConfigUtils.getOrElse(config, HADOOP_HOME, System.getenv("HADOOP_HOME")));
        env.put("SPARK_HOME", ConfigUtils.getOrElse(config, SPARK_HOME, System.getenv("SPARK_HOME")));
        return env;
    }
}
