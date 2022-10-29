package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.Content;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.LearningTask;
import pers.juumii.MindTrace.model.data.Level;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

import java.util.ArrayList;

class KnowledgeMapperTest {

    public static final KnowledgeMapper knowledgeMapper = SqlSessionUtils.getSqlSession().getMapper(KnowledgeMapper.class);
    public static final LearningTaskMapper learningTaskMapper = SqlSessionUtils.getSqlSession().getMapper(LearningTaskMapper.class);
    public static final ContentMapper contentMapper = SqlSessionUtils.getSqlSession().getMapper(ContentMapper.class);

    @Test
    void insertKnowledge() {

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
        Knowledge knowledge = knowledgeMapper.queryById(66);
        knowledge.setDesc("test update");
        knowledgeMapper.update(knowledge);
    }

}