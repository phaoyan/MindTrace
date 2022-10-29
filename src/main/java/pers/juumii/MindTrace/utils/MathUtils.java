package pers.juumii.MindTrace.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathUtils {

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

    //length用户要求产生字符串的长度
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args){
        System.out.println(randomIndexes(4, 10, 4));
    }
}
