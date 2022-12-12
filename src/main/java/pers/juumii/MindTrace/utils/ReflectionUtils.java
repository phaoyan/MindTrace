package pers.juumii.MindTrace.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

    public static <T> List<Method> getters(Class<T> cl){
        List<Method> res = new ArrayList<>();
        for(Method method: cl.getDeclaredMethods())
            if(method.getName().startsWith("get") && !Modifier.isStatic(method.getModifiers()))
                res.add(method);
        return res;
    }

    public static <T> List<Method> setters(Class<T> cl){
        List<Method> res = new ArrayList<>();
        for(Method method: cl.getDeclaredMethods())
            if(method.getName().startsWith("set") && !Modifier.isStatic(method.getModifiers()))
                res.add(method);
        return res;
    }
}
