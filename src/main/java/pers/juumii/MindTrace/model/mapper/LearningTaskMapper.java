package pers.juumii.MindTrace.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.juumii.MindTrace.model.data.LearningTask;
import pers.juumii.MindTrace.model.mapper.utils.DataMapper;

import java.util.List;

public interface LearningTaskMapper extends DataMapper<LearningTask> {

    int updateKnowledgeId(@Param("knowledgeId") int knowledgeId, @Param("id") int id);
    List<LearningTask> queryByKnowledgeId(@Param("knowledgeId") int knowledgeId);
    List<LearningTask> queryBySuperTaskId(@Param("superTaskId") int superTaskId);
}
