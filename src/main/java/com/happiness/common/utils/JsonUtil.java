package com.happiness.common.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtil {
    public final static ObjectMapper objectMapper = new ObjectMapper();
    public final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    public final static TypeFactory typeFactory;

    static {
        objectMapper.setLocale(Locale.CHINA);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(dateFormat);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);// 默认不显示无JsonView注解的字段
        // objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL,
        // As.EXISTING_PROPERTY);// 启用默认类型作为属性@class
        objectMapper.disableDefaultTyping();// 禁用默认类型作为属性
        // objectMapper.enableDefaultTypingAsProperty(DefaultTyping.NON_FINAL,
        // "@demoType");
        // 忽略Null
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);// 允许使用非双引号属性名
        objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);// 允许单引号包信属性名
        objectMapper.configure(Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);// 允许JSON整数以多个0开始
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false);
        typeFactory = objectMapper.getTypeFactory();
    }

    public static String stringify(Object obj) throws JsonProcessingException {
        if (obj instanceof String) {
            return obj.toString();
        }
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T parse(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(json, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(String json, JavaType javaType)
            throws JsonParseException, JsonMappingException, IOException {
        return (T) objectMapper.readValue(json, javaType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(String json, TypeReference<?> typeReference)
            throws JsonParseException, JsonMappingException, IOException {
        return (T) objectMapper.readValue(json, typeReference);
    }

    public static Map<String, String> parseToMap(String json) throws JsonProcessingException, IOException {
        JsonNode node = objectMapper.readTree(json);
        Iterator<Entry<String, JsonNode>> fields = node.fields();
        Map<String, String> map = new HashMap<String, String>();
        while (fields.hasNext()) {
            Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode jn = field.getValue();
            String value = null;
            if (jn != null && !jn.isNull()) {
                value = jn.asText();
                if (!jn.isValueNode()) {
                    value = stringify(jn);
                }
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<String> parseToList(String json) throws JsonProcessingException, IOException {
        List<String> list = new ArrayList<String>();
        JsonNode node = objectMapper.readTree(json);
        Iterator<JsonNode> it = node.iterator();
        while (it.hasNext()) {
            JsonNode jn = it.next();
            list.add(jn.toString());
        }
        return list;
    }

    public static List<JsonNode> parseToJsonNodeList(String json) throws JsonProcessingException, IOException {
        List<JsonNode> list = new ArrayList<JsonNode>();
        JsonNode node = objectMapper.readTree(json);
        Iterator<JsonNode> it = node.iterator();
        while (it.hasNext()) {
            JsonNode jn = it.next();
            list.add(jn);
        }
        return list;
    }

    public static <T> T changeType(Object obj, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        return parse(stringify(obj), clazz);
    }

    public static ArrayType constructArrayType(Class<?> elementType) {
        return typeFactory.constructArrayType(elementType);
    }

    public static CollectionType constructCollectionType(
            @SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return typeFactory.constructCollectionType(collectionClass, elementClass);
    }

    public static MapType constructMapType(@SuppressWarnings("rawtypes") Class<? extends Map> mapClass,
            Class<?> keyClass, Class<?> valueClass) {
        return typeFactory.constructMapType(mapClass, keyClass, valueClass);
    }
}
