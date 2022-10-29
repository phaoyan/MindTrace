package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.LearningRecord;
import pers.juumii.MindTrace.model.data.Level;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

import java.time.LocalDateTime;

class LearningRecordMapperTest {

    public static final LearningRecordMapper mapper = SqlSessionUtils.getMapper(LearningRecordMapper.class);
    @Test
    void insertRecord() {
    }

    @Test
    void queryRecords(){
        LearningRecordMapper mapper = SqlSessionUtils.getSqlSession().getMapper(LearningRecordMapper.class);
        System.out.println(mapper.queryAll());
    }

    @Test
    void queryRecordsByTaskId(){
        LearningRecordMapper mapper = SqlSessionUtils.getSqlSession().getMapper(LearningRecordMapper.class);
        System.out.println(mapper.queryByTaskId(1).get(0));
    }

    @Test
    void update(){
        LearningRecord learningRecord = mapper.queryById(1);
        learningRecord.setCompletion(100);
        System.out.println(mapper.update(learningRecord));
    }
}