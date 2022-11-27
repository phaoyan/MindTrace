package pers.juumii.MindTrace.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
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
    //对符合条件的数据进行某些操作，这些操作无返回值
    public static <T> void forAllIf(Collection<T> collection, Function<T, Boolean> predicate, Consumer<T> operation){
        for(T e: collection)
            if(predicate.apply(e))
                operation.accept(e);
    }

    public static <T> Stack<T> stackOf(Collection<T> collection){
        Stack<T> stack = new Stack<>();
        collection.forEach(stack::push);
        return stack;
    }

    public static <T> Stack<T> randomStackOf(Collection<T> collection){
        ArrayList<T> ori = new ArrayList<>(collection);
        List<Integer> randomIndexes = MathUtils.randomIndexes(0, ori.size() - 1, ori.size());
        Stack<T> stack = new Stack<>();
        randomIndexes.forEach(index -> stack.push(ori.get(index)));
        return stack;
    }
}
