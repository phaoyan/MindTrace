package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

class KnowledgeMapperTest {

    public static final KnowledgeMapper knowledgeMapper = SqlSessionUtils.getSqlSession().getMapper(KnowledgeMapper.class);
    public static final LearningCardMapper LEARNING_CARD_MAPPER = SqlSessionUtils.getSqlSession().getMapper(LearningCardMapper.class);

    @Test
    void insertKnowledge() {
        Knowledge data = new Knowledge();
        data.setId(1);
        data.setSuperKnowledgeId(5);
        data.setMasteryMin(10);
        data.setMasteryMax(100);
        data.setDescription("test knowldege");
        knowledgeMapper.insert(data);
    }

    @Test
    void queryKnowledgeById() {
        System.out.println(knowledgeMapper.queryById(66));
    }

    @Test
    void queryAll() {
    }

    @Test
    void update(){
        Knowledge knowledge = knowledgeMapper.queryById(1);
        knowledge.setDescription("test update");
        knowledgeMapper.update(knowledge);
    }

}