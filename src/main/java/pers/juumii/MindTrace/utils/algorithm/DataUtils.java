package pers.juumii.MindTrace.utils.algorithm;

import java.util.*;
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
        if(collection == null)
            return res;
        for(T e: collection)
            if(predicate.apply(e))
                res.add(e);
        return res;
    }
    public static <T, R> List<R> destructureAll(Collection<T> collection, Function<T, R> transformation){
        List<R> res = new ArrayList<>();
        for(T item: collection)
            res.add(transformation.apply(item));
        return res;
    }

    //对符合条件的数据进行某些操作，这些操作无返回值
    public static <T> void forAllIf(Collection<T> collection, Function<T, Boolean> predicate, Consumer<T> operation){
        for(T e: collection)
            if(predicate.apply(e))
                operation.accept(e);
    }

    public static <T> boolean ifAll(Collection<T> collection, Function<T, Boolean> predicate){
        for (T e: collection)
            if(!predicate.apply(e))
                return false;
        return true;
    }

    public static <T> boolean satisfy(Collection<T> collection, Function<Collection<T>, Boolean> predicate){
        if(collection.isEmpty())
            return false;
        return predicate.apply(collection);
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

    public static <T> Stack<T> randomStackOf(Collection<T> collection, long seed){
        ArrayList<T> ori = new ArrayList<>(collection);
        List<Integer> randomIndexes = MathUtils.randomIndexes(0, ori.size() - 1, ori.size(), seed);
        Stack<T> stack = new Stack<>();
        randomIndexes.forEach(index -> stack.push(ori.get(index)));
        return stack;
    }

    public static <T> T getLast(List<T> list){
        return list.isEmpty() ? null: list.get(list.size() - 1);
    }

    public static <T> List<T> join(List<List<T>> groups) {
        List<T> res = new ArrayList<>();
        groups.forEach(res::addAll);
        return res;
    }

    public static <T> List<T> reverse(List<T> list) {
        Stack<T> ori = stackOf(list);
        Stack<T> reverse = new Stack<>();
        while (!ori.isEmpty())
            reverse.push(ori.pop());
        return reverse;
    }

    public static <T> List<T> difference(List<T> first, List<T> second) {
        return getAllIf(first, element -> !second.contains(element));
    }

    public static <T> List<T> intersection(List<T> first, List<T> second) {
        return getAllIf(first, second::contains);
    }

    @SafeVarargs
    public static <T> void layeredSort(List<T> list, Comparator<T>... comparators){
        //将这些comparators按序组合为一个新的comparator：如果第一个比较相等则考虑第二个，如果第二个比较相等则考虑第三个，以此类推
        list.sort((o1, o2) -> {
            for (Comparator<T> comparator: comparators)
                if(comparator.compare(o1, o2) != 0)
                    return comparator.compare(o1, o2);
            return 0;
        });
    }

    public static <T> void weightedSort(List<T> list, Map<Function<T,Double>, Double> weightMap){
        list.sort(Comparator.comparing(element -> {
            double rate = 0;
            for(Function<T, Double> evaluator: weightMap.keySet())
                rate += evaluator.apply(element) * weightMap.get(evaluator);
            return rate;
        }));
    }

    public static <K, T> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> assign) {
        Map<K, List<T>> res = new HashMap<>();
        for(T element: collection){
            K key = assign.apply(element);
            if(res.containsKey(key))
                res.get(key).add(element);
            else res.put(key, new ArrayList<>(List.of(element)));
        }
        return res;
    }
}
