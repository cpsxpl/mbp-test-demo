package com.mbp.eng.framework.common.util.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class EntityUtils {
    private static Logger logger = LoggerFactory.getLogger(EntityUtils.class);
    /**
     * @param entity    对象
     * @param fieldName 对象中某属性名称
     * @return
     */
    public boolean checkFields(Object entity, String fieldName) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        } catch (SecurityException e) {
            logger.warn("发生异常：" + e.getMessage());
        }
        return true;
    }
}
