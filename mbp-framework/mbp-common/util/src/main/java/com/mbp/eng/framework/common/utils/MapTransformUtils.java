package com.mbp.eng.framework.common.utils;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: MapTransformUtils
 * @Description: TODO(map和bean之间相互转换)
 */
public class MapTransformUtils {
    /**
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     * @Title: mapToObject
     * @Description: TODO(map转换为bean)
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null) {
            return null;
        }

        T obj = beanClass.newInstance();
        BeanUtils.populate(obj, map);

        return obj;
    }

    /**
     * @param object
     * @return
     * @Title: objectToMap
     * @Description: TODO(bean转换为Map)
     */
    public static Map<?, ?> objectToMap(Object object) {
        if (object == null) {
            return null;
        }
        return new BeanMap(object);
    }

    //*********************************************使用 reflect 反射进行转换_start*********************************************
    public static <T> T mapToObjectrReflect(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null)
            return null;

        T t = beanClass.newInstance();

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            field.setAccessible(true);
            field.set(t, map.get(field.getName()));
        }

        return t;
    }

    public static Map<String, Object> objectToMapReflect(Object object) throws Exception {
        if (object == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }

        return map;
    }
    //*********************************************使用 reflect 反射进行转换_end***********************************************

    //*********************************************使用 Introspector 反射进行转换_start****************************************
    public static <T> T mapToObjectIntrospector(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null)
            return null;

        T t = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(t, map.get(property.getName()));
            }
        }

        return t;
    }

    public static Map<String, Object> objectToMapIntrospector(Object object) throws Exception {
        if (object == null)
            return null;

        Map<String, Object> map = new HashMap<String, Object>();

        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(object) : null;
            map.put(key, value);
        }

        return map;
    }
    //*********************************************使用 Introspector 反射进行转换_end******************************************

    public static <T> T getObject(Map<String, Object> map, Class<T> clz) {
        T t = null;
        try {
            //创建对象中的所有字段
            t = clz.newInstance();
            //获取对象中的所有字段
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                String fname = field.getName();
                Object object = map.get(fname);
                //设置属性可访问
                field.setAccessible(true);
                //为目标对象设置值
                field.set(t, object);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将List中的Key转换为小写
     *
     * @param list 返回新对象
     * @return
     */
    public static List<Map<String, Object>> convertKeyList2LowerCase(List<Map<String, Object>> list) {
        if (null == list) {
            return null;
        }
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //
        Iterator<Map<String, Object>> iteratorL = list.iterator();
        while (iteratorL.hasNext()) {
            Map<String, Object> map = (Map<String, Object>) iteratorL.next();
            //
            Map<String, Object> result = convertKey2LowerCase(map);
            if (null != result) {
                resultList.add(result);
            }
        }
        //
        return resultList;
    }

    /**
     * 转换单个map,将key转换为小写.
     *
     * @param map 返回新对象
     * @return
     */
    public static Map<String, Object> convertKey2LowerCase(Map<String, Object> map) {
        if (null == map) {
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        //
        Set<String> keys = map.keySet();
        //
        Iterator<String> iteratorK = keys.iterator();
        while (iteratorK.hasNext()) {
            String key = (String) iteratorK.next();
            Object value = map.get(key);
            if (null == key) {
                continue;
            }
            //
            String keyL = key.toLowerCase();
            result.put(keyL, value);
        }
        return result;
    }

    /**
     * 将List中Map的Key转换为小写.
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> trimListKeyValue(List<Map<String, Object>> list) {
        if (null == list) {
            return null;
        }
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //
        Iterator<Map<String, Object>> iteratorL = list.iterator();
        while (iteratorL.hasNext()) {
            Map<String, Object> map = (Map<String, Object>) iteratorL.next();
            //
            Map<String, Object> result = trimKeyValue(map);
            if (null != result) {
                resultList.add(result);
            }
        }
        //
        return resultList;
    }

    /**
     * 转换单个map,将key转换为小写.
     *
     * @param map 返回新对象
     * @return
     */
    public static Map<String, Object> trimKeyValue(Map<String, Object> map) {
        if (null == map) {
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        //
        Set<String> keys = map.keySet();
        //
        Iterator<String> iteratorK = keys.iterator();
        while (iteratorK.hasNext()) {
            String key = (String) iteratorK.next();
            Object value = map.get(key);
            if (null == key) {
                continue;
            }
            //
            String keyT = key.trim();
            if (value instanceof String) {
                String valueT = String.valueOf(value).trim();
                result.put(keyT, valueT);
            } else {
                result.put(keyT, value);
            }
        }
        return result;
    }
}