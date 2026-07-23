package com.mbp.eng.framework.common.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JSON转换工具
 */
public class JSONUtilsV1 {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtilsV1.class);

    private static final JsonFactory jsonFactory = new JsonFactory();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final com.fasterxml.jackson.databind.ObjectMapper MAPPER = new com.fasterxml.jackson.databind.ObjectMapper();
    public static final Gson gson = new Gson();
    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        MAPPER.setDateFormat(fmt);
    }


    private JSONUtilsV1() {
    }

    public static <T> T deserialize(byte[] value, Class<T> clazz) throws IOException {
        return MAPPER.readValue(value, clazz);
    }

    public static byte[] serializeAsBytes(Object object) {
        try {
            return MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            return new byte[]{};
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) throws IOException {
        return MAPPER.readValue(json, clazz);
    }

    public static <T> T deserialize(String content, com.fasterxml.jackson.core.type.TypeReference<T> valueTypeRef) throws IOException {
        return MAPPER.readValue(content, valueTypeRef);
    }

    public static String serialize(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "serialize object to json error : " + e.getMessage();
        }
    }

    /**
     * 把一个对象转换成json字符串
     *
     * @param obj
     * @param prettyPrint,设置转换后的json样式<br/> true：可读性强的json串,如：<br/>
     *                                      {<br/>
     *                                      "key1" : "value1",<br/>
     *                                      "key2" : "value2"<br/>
     *                                      }<br/>
     *                                      fasle：精简的json串,如：<br/>
     *                                      {"key1":"value1","key2":"value2"}
     * @return
     */
    public static String toJSON(Object obj, boolean prettyPrint) throws Exception {
        if (obj == null) {
            return null;
        }

        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(writer);
        if (prettyPrint) {
            jsonGenerator.useDefaultPrettyPrinter();
        }

        objectMapper.writeValue(jsonGenerator, obj);
        return writer.toString();
    }

    /**
     * 把一个对象转换成json字符串,默认采用精简模式
     *
     * @param obj
     * @return
     * @see #toJSON(Object, boolean)
     */
    public static String toJSON(Object obj) throws Exception {
        return toJSON(obj, false);
    }

    /**
     * 解析json字符串,该json字符串符合如下格式：<br/>
     * [{key:value}]<br/>
     * 比如：[{\"id\":\"123456\",\"name\":\"张三\"}, {\"id\":\"123457\",\"name\":\"李四\"}]<br/>
     *
     * @param json
     * @return ArrayList<Map < String, Object>>
     * @throws Exception,如果解析错误
     */
    public static ArrayList<Map<String, Object>> parseJSON(String json) throws Exception {
        if (json == null || json.length() == 0) {
            return null;
        }

        return objectMapper.readValue(json, new TypeReference<ArrayList<Map<Object, Object>>>() {
        });
    }

    /**
     * 解析json字符串,该json字符串符合如下格式：<br/>
     * {key:value}<br/>
     * 比如：{\"id\":\"123456\",\"name\":\"张三\",\"borther\":[\"李四\",\"王五\"]}<br/>
     *
     * @param json
     * @return Map<String, Object>
     * @throws Exception,如果解析错误
     */
    public static Map<String, Object> parseJSON2Map(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Object fromJson(String jsonAsString, Class<T> pojoClass) {
        try {
            return objectMapper.readValue(jsonAsString, pojoClass);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List getList(String key, String jsonString) {
        List list = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                String msg = jsonArray.getString(i);
                list.add(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List listKeyMaps(String key, String jsonString) {
        List list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map map = new HashMap();
                Iterator iterator = jsonObject2.keys();
                while (iterator.hasNext()) {
                    String jsonKey = iterator.next() + "";
                    Object objectValue = jsonObject2.get(jsonKey);
                    if (objectValue == null) {
                        objectValue = "";
                    }
                    map.put(jsonKey, objectValue);
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
