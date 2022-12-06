package pers.juumii.MindTrace.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class JsonUtils {
    public static <T> String toJson(T origin){
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(origin);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
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
