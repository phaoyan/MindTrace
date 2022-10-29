package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.LearningTask;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

import java.util.ArrayList;
import java.util.List;

class LearningTaskMapperTest {

    public static final LearningTaskMapper mapper = SqlSessionUtils.getSqlSession().getMapper(LearningTaskMapper.class);
    @Test
    void insertLearningTask() {

    }

    @Test
    void queryLearningTasksByKnowledgeId() {
        System.out.println(mapper.queryByKnowledgeId(2));
    }

    @Test
    void queryLearningTasksById() {
    }

    @Test
    void update(){
        LearningTask learningTask = mapper.queryById(10);
        learningTask.setDesc("test update learning task");
        mapper.update(learningTask);
    }
}