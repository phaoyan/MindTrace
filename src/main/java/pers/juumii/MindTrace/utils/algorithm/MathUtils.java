package pers.juumii.MindTrace.utils.algorithm;

import pers.juumii.MindTrace.utils.Constants;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathUtils {

    public static long unique(){
        return Duration.between(LocalDateTime.now(), Constants.timeAnchor).toNanos();
    }

    /**
     * 生成从L到R的count个不重复随机数
     */
    public static List<Integer> randomIndexes(int L, int R, int count){
        assert count <= R - L + 1;
        List<Integer> res = new ArrayList<>();
        while(res.size() < count){
            int rand = new Random().nextInt(R - L + 1) + L;
            if(!res.contains(rand))
                res.add(rand);
        }
        return res;
    }

    public static List<Integer> randomIndexes(int L, int R, int count, long seed){
        assert count <= R - L + 1;
        List<Integer> res = new ArrayList<>();

        long tempSeed = seed;
        while(res.size() < count){
            int rand = new Random(tempSeed).nextInt(R - L + 1) + L;
            tempSeed = new Random(tempSeed).nextInt();
            if(!res.contains(rand))
                res.add(rand);
        }
        return res;
    }

    //length用户要求产生字符串的长度
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
