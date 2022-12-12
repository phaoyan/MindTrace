package pers.juumii.MindTrace.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pers.juumii.MindTrace.model.data.InstantData;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonUtils {

    public static String getterNameToFieldName(String getterName){
        return getterName.startsWith("get") ? new StringBuilder().append(getterName).delete(0,4).insert(0, Character.toLowerCase(getterName.charAt(3))).toString() : getterName;
    }

    //将包括private的全部属性和getter序列化。用于向前端分发完整的数据，包括即时生成的计算数据
    public static <T> String toJson(T origin){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()).setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            return mapper.writeValueAsString(origin);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //不将private的属性和getter序列化。用于数据持久化，这样可以减小文件大小
    public static <T> String toJsonShallowly(T origin){
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(toJsonNodeShallowly(origin));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> JsonNode toJsonNodeShallowly(T origin){
        if (origin == null)
            return null;

        if(origin instanceof List<?>){
            ArrayNode res = new ArrayNode(JsonNodeFactory.instance);
            ((List<?>) origin).forEach(element -> res.add(toJsonNodeShallowly(element)));
            return res;
        }

        if(origin instanceof Map<?,?>){
            ObjectNode res = new ObjectNode(JsonNodeFactory.instance);
            ((Map<?, ?>) origin).forEach((key, value) -> res.set((String) key, toJsonNodeShallowly(value)));
            return res;
        }

        if(!origin.getClass().isAnnotationPresent(InstantData.class))
            return readJson(toJson(origin), JsonNode.class);

        ObjectNode res = new ObjectNode(JsonNodeFactory.instance);
        List<Method> concernedGetters = DataUtils.getAllIf(ReflectionUtils.getters(origin.getClass()), method -> !method.isAnnotationPresent(InstantData.class));
        for (Method getter: concernedGetters)
            try {
                res.set(getterNameToFieldName(getter.getName()), toJsonNodeShallowly(getter.invoke(origin)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        return res;
    }

    public static <T> T readJson(String json, Class<T> cl) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false).readValue(json, cl);
        } catch (JsonProcessingException ignored) {
        } catch (IllegalArgumentException e){
            try {
                return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false).readValue("{}", cl);
            } catch (JsonProcessingException ex) {
                try {
                    return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false).readValue("[]", cl);
                } catch (JsonProcessingException exc) {
                    exc.printStackTrace();
                }
            }
        }
        if(Objects.requireNonNull(readJson(json, ObjectNode.class)).isEmpty()) {
            try {
                return cl.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
