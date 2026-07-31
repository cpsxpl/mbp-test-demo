package com.mbp.eng.framework.common.util.obj;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbp.eng.framework.common.util.date.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CheckObjectUtils {
    private static final Logger logger = LoggerFactory.getLogger(CheckObjectUtils.class);

    /***********************↓↓↓↓↓↓单例(双重校验锁)↓↓↓↓↓↓↓*****************/
    private static volatile CheckObjectUtils instance;

    private CheckObjectUtils() {
        long time = System.currentTimeMillis();
        logger.info("==========单例(双重校验锁)_time:{}", DateUtil.getFormatTime(time));
    }

    public static CheckObjectUtils getInstance() {
        if (instance == null) {
            synchronized (CheckObjectUtils.class) {
                if (instance == null)
                    //instance为 volatile,现在没问题了
                    instance = new CheckObjectUtils();
            }
        }
        return instance;
    }

    /***********************↑↑↑↑↑↑单例(双重校验锁)↑↑↑↑↑↑↑*****************/

    private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 泛型方法:接收 Map 数据和目标 Class 类型
    public static <T> T convertMap(Map<String, Object> data, Class<T> clazz) throws Exception {
        // 1. 实例化对象
        T instance = clazz.getDeclaredConstructor().newInstance();
        // 2. 遍历数据并赋值给对象的属性
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            try {
                Field field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true); // 允许访问私有属性
                field.set(instance, entry.getValue());
            } catch (NoSuchFieldException e) {
                // 忽略 Map 中多余的、实体类中不存在的字段
            }
        }
        return instance;
    }

    // 泛型方法:接收 JSON 数据和目标 Class 类型
    public static <T> T convertJson(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }

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
        boolean flag = true; //定义返回结果,默认为true
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

    public void serialize(LocalDateTime value, JsonGenerator gen) {
        String fieldName = gen.getOutputContext().getCurrentName();
        if (fieldName != null) {
            Object currentValue = gen.getOutputContext().getCurrentValue();
            if (currentValue != null) {
                Class<?> clazz = currentValue.getClass();
                Map<String, Field> fieldMap = FIELD_CACHE.computeIfAbsent(clazz, this::buildFieldMap);
                Field field = fieldMap.get(fieldName);
                if (field != null && field.isAnnotationPresent(JsonFormat.class)) {
                    JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(jsonFormat.pattern());
                        gen.writeString(formatter.format(value));
                        return;
                    } catch (Exception ex) {
                        logger.warn("[serialize][({}#{}) 使用 JsonFormat pattern 失败,尝试使用默认的 Long 时间戳]",
                                clazz.getName(), fieldName, ex);
                    }
                }
            }
        }
    }

    /**
     * 构建字段映射（缓存）
     *
     * @param clazz 类
     * @return 字段映射
     */
    private Map<String, Field> buildFieldMap(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : ReflectUtil.getFields(clazz)) {
            String fieldName = field.getName();
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (jsonProperty != null) {
                String value = jsonProperty.value();
                if (StrUtil.isNotEmpty(value) && ObjUtil.notEqual("\u0000", value)) {
                    fieldName = value;
                }
            }
            fieldMap.put(fieldName, field);
        }
        return fieldMap;
    }

    public void test() {
        // 方法1：通过System.getProperty获取类路径
        String classpath = System.getProperty("java.class.path");
        System.out.println("方法1 - 系统类路径: " + classpath);

        // 方法2：通过当前类的ClassLoader获取资源路径
        URL resource = CheckObjectUtils.class.getResource("");
        System.out.println("方法2 - 当前类所在路径: " + resource.getPath());

        // 方法3：通过ClassLoader获取根路径
        URL rootResource = CheckObjectUtils.class.getClassLoader().getResource("");
        if (rootResource != null) {
            System.out.println("方法3 - 类加载器根路径: " + rootResource.getPath());
        }

        // 方法4：获取当前工作目录
        String workingDir = System.getProperty("user.dir");
        System.out.println("方法4 - 当前工作目录: " + workingDir);

        // 方法5：使用File获取规范路径
        try {
            File file = new File(".");
            System.out.println("方法5 - 当前目录规范路径: " + file.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
