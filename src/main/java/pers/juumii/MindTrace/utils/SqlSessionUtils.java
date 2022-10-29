package pers.juumii.MindTrace.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

public class SqlSessionUtils {
    public static final SqlSession sqlSession;

    static {
        try {
            sqlSession = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml")).openSession(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SqlSession getSqlSession(){
        return sqlSession;
    }
    public static <T> T getMapper(Class<T> mapperClass){
        return getSqlSession().getMapper(mapperClass);
    }
}
