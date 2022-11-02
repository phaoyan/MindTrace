package pers.juumii.MindTrace.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class JsonUtils {
    public static <T> String toJson(T origin){
        try {
            return new ObjectMapper().writeValueAsString(origin);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readJson(String json, Class<T> cl) {
        try {
            return new ObjectMapper().readValue(json, cl);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
