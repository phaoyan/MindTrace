package pers.juumii.MindTrace.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class DataUtils {
    public static  <T> T getIf(Collection<T> collection, Function<T, Boolean> predicate){
        T res = null;
        for(T e: collection)
            if(predicate.apply(e))
                res = e;

        return res;
    }
    public static  <T> List<T> getAllIf(Collection<T> collection, Function<T, Boolean> predicate){
        List<T> res = new ArrayList<>();
        for(T e: collection)
            if(predicate.apply(e))
                res.add(e);
        return res;
    }
}
