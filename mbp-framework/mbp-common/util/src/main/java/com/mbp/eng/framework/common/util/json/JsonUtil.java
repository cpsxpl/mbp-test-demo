package com.mbp.eng.framework.common.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mbp.eng.framework.common.exception.ErrorStatus;
import com.mbp.eng.framework.common.exception.MbpException;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static final ObjectMapper objectMapper = new ObjectMapper();

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        objectMapper.setDateFormat(simpleDateFormat);
    }

    private JsonUtil() {
    }

    public static <T> T deserialize(byte[] bytes, Class<T> tClass) {
        try {
            return objectMapper.readValue(bytes, tClass);
        } catch (IOException e) {
            logger.error("deserialize by byte key error : " + e.getMessage(), e);
            throw new MbpException(ErrorStatus.SERVER_INTERNAL_ERROR, e.getMessage(), e);
        }
    }

    public static byte[] serializeAsBytes(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            logger.error("serializeAsBytes " + object + " error : " + e.getMessage(), e);
            throw new MbpException(ErrorStatus.SERVER_INTERNAL_ERROR, e.getMessage(), e);
        }
    }

    public static <T> T deserialize(byte[] bytes, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(bytes, valueTypeRef);
        } catch (IOException e) {
            logger.error(valueTypeRef.getClass().getName() + " deserialize error : " + e.getMessage(), e);
            throw new MbpException(ErrorStatus.SERVER_INTERNAL_ERROR, e.getMessage(), e);
        }
    }

    public static <T> T deserialize(String json, Class<T> tClass) {
        try {
            return objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("deserialize " + json + " error : " + e.getMessage(), e);
            throw new MbpException(ErrorStatus.SERVER_INTERNAL_ERROR, e.getMessage(), e);
        }
    }

    public static <T> T deserialize(String json, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(json, valueTypeRef);
        } catch (IOException e) {
            logger.error(valueTypeRef.getClass().getName() + " deserialize " + json + " error : " + e.getMessage(), e);
            throw new MbpException(ErrorStatus.SERVER_INTERNAL_ERROR, e.getMessage(), e);
        }
    }

    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("serialize " + object + " error : " + e.getMessage(), e);
            throw new MbpException(ErrorStatus.SERVER_INTERNAL_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 把一个对象转换成json字符串，默认采用精简模式
     *
     * @param object
     * @return
     * @see #toJSON(Object, boolean)
     */
    public static String toJSON(Object object) {
        return toJSON(object, true);
    }

    public static String toJSON(Object object, boolean prettyPrint) {
        try {
            if (prettyPrint) {
                ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
                return objectWriter.writeValueAsString(object);
            } else {
                return objectMapper.writeValueAsString(object);
            }
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    /*public static String toJSON(Object obj, boolean prettyPrint) throws Exception {
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
    }*/

    //取出json中某filed
    public static String get(String json, String filed) {
        JsonNode readTree = null;
        try {
            readTree = objectMapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return readTree.isNull() ? null : readTree.get(filed).toString();
    }

    /**
     * 取出json中某filed,取到的值是textValue,外面不包含双引号
     *
     * @param json
     * @param filed
     * @return
     */
    public static String getFiledTextValue(String json, String filed) {
        JsonNode readTree = null;
        try {
            readTree = objectMapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return readTree.isNull() ? null : readTree.get(filed).asText();
    }

    //获取json tree
    public static JsonNode getJsonNode(String json) {
        JsonNode readTree = null;
        try {
            readTree = objectMapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return readTree;
    }

    public static List getList(JsonNode jsonNode) {
        List list = new ArrayList<>();
        if (jsonNode.isArray()) {
            for (final JsonNode objNode : jsonNode) {
                list.add(objNode.asText());
            }
        }
        return list;
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

    public static boolean isJson(String json) {
        Gson gson = new Gson();
        try {
            gson.fromJson(json, Object.class);
            return true;
        } catch (JsonSyntaxException jsonSyntaxException) {
            return false;
        }
    }

    public static boolean isJson1(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException jsonException) {
            try {
                new JSONArray(json);
            } catch (JSONException jsonException1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    public static boolean isGoodJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        new JsonParser().parse(json);
        return true;
    }

    public static void test() throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", "a");
        jsonObject.put("B", "B");
        jsonObject.put("C", "C");
        jsonObject.put("D", "D");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("a2", "a2");
        jsonObject2.put("B2", "B2");
        jsonObject2.put("C2", "C2");
        jsonObject2.put("D2", "D2");
        jsonObject.put("obj", jsonObject2);
        jsonObject.put("arr", Collections.singletonList(jsonObject2));

        JsonNode jsonNode = objectMapper.readTree(toJSON(jsonObject));

        boolean missingNode1 = jsonNode.findPath("aaaa").isNull(); //false
        boolean missingNode2 = jsonNode.findPath("aaaa").isEmpty(); //true
        boolean missingNode3 = jsonNode.findPath("aaaa").isMissingNode(); //true
        String missingNode4 = jsonNode.findPath("aaaa").asText(); //""
        boolean missingNode5 = jsonNode.findPath("aaaa").isArray(); //false
        boolean missingNode6 = jsonNode.findPath("aaaa").isObject(); //false
        boolean child1 = jsonNode.findPath("a").isNull(); //false
        boolean child2 = jsonNode.findPath("a").isEmpty(); //true
        boolean child3 = jsonNode.findPath("a").isMissingNode(); //false
        String child4 = jsonNode.findPath("a").asText(); //"a"
        boolean child5 = jsonNode.findPath("aaaa").isArray(); //false
        boolean child6 = jsonNode.findPath("aaaa").isObject(); //false
        boolean son1 = jsonNode.findPath("a2").isNull(); //false
        boolean son2 = jsonNode.findPath("a2").isEmpty(); //true
        boolean son3 = jsonNode.findPath("a2").isMissingNode(); //false
        String son4 = jsonNode.findPath("a2").asText(); //"a2"
        boolean son5 = jsonNode.findPath("aaaa").isArray(); //false
        boolean son6 = jsonNode.findPath("aaaa").isObject(); //false
        boolean obj1 = jsonNode.findPath("obj").isNull(); //false
        boolean obj2 = jsonNode.findPath("obj").isEmpty(); //false
        boolean obj3 = jsonNode.findPath("obj").isMissingNode(); //false
        String obj4 = jsonNode.findPath("obj").asText(); //""
        boolean obj5 = jsonNode.findPath("obj").isArray(); //false
        boolean obj6 = jsonNode.findPath("obj").isObject(); //true
        boolean arr1 = jsonNode.findPath("arr").isNull(); //false
        boolean arr2 = jsonNode.findPath("arr").isEmpty(); //false
        boolean arr3 = jsonNode.findPath("arr").isMissingNode(); //false
        String arr4 = jsonNode.findPath("arr").asText(); //""
        boolean arr5 = jsonNode.findPath("arr").isArray(); //true
        boolean arr6 = jsonNode.findPath("arr").isObject(); //false
    }
}
