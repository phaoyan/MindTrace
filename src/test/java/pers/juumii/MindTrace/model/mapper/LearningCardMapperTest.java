package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.LearningCard;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

class LearningCardMapperTest {

    public static final LearningCardMapper mapper = SqlSessionUtils.getSqlSession().getMapper(LearningCardMapper.class);
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
        LearningCard learningCard = mapper.queryById(10);
        learningCard.setDescription("test update learning task");
        mapper.update(learningCard);
    }
}