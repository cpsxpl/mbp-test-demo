package com.mbp.eng.framework.common.util.obj;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class CheckObjectUtils {
    private static final Logger logger = LoggerFactory.getLogger(CheckObjectUtils.class);

    /**
     * 判断Object对象不为空且为数字
     *
     * @param object
     * @return true非空 false空
     */
    public static boolean isNotNullNum(Object object) {
        if (StringUtils.isNotBlank(String.valueOf(object)) && !String.valueOf(object).equalsIgnoreCase("null") &&
                String.valueOf(object).matches("[0-9]{1,}")
        ) {
            return true;
        }
        return false;
    }

    /**
     * 判断该对象是否: 返回ture表示所有属性为null  返回false表示不是所有属性都是null
     */
    public static boolean isAllFieldNull(Object obj) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field[] fs = aClass.getDeclaredFields();
        boolean flag = true;
        for (Field f : fs) {
            f.setAccessible(true);
            Object object = f.get(obj);
            if (object != null) {
                flag = false;
            }
        }
        return flag;
    }
    /**
     * 判断对象是否为空,且对象的所有属性都为空
     * boolean类型会有默认值false 判断结果不会为null 会影响判断结果
     * 序列化的默认值也会影响判断结果
     *
     * @param object
     * @return
     */
    public static boolean objCheckIsNull(Object object) {
        Class clazz = (Class) object.getClass(); // 得到类对象
        Field[] fields = clazz.getDeclaredFields(); // 得到所有属性
        boolean flag = true; //定义返回结果，默认为true
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(object); //得到属性值
                Type fieldType = field.getGenericType(); //得到属性类型
                String fieldName = field.getName(); //得到属性名
                System.out.println("属性类型：" + fieldType + ",属性名：" + fieldName + ",属性值：" + fieldValue);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (fieldValue != null) {  //只要有一个属性值不为null 就返回false 表示对象不为null
                flag = false;
                break;
            }
        }
        return flag;
    }
}
