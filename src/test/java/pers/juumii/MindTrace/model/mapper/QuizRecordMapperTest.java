package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.QuizRecord;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

import java.util.List;

class QuizRecordMapperTest {

    public static final QuizRecordMapper mapper = SqlSessionUtils.getMapper(QuizRecordMapper.class);
    @Test
    void insertRecord() {

    }

    @Test
    void queryRecords(){
        System.out.println(mapper.queryAll());
    }

    @Test
    void queryRecordsByTaskId(){
        System.out.println(mapper.queryByTaskId(1).get(0));
    }

    @Test
    void testGeneric(){
        System.out.println(List.class.getName());
    }

    @Test
    void update(){
        QuizRecord quizRecord = mapper.queryById(1);
        quizRecord.setDescription("test update record");
        mapper.update(quizRecord);
    }
}