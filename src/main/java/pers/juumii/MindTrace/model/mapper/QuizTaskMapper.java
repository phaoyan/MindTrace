package pers.juumii.MindTrace.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.juumii.MindTrace.model.data.QuizTask;
import pers.juumii.MindTrace.model.mapper.utils.DataMapper;

import java.util.List;

public interface QuizTaskMapper extends DataMapper<QuizTask> {

    List<QuizTask> queryByKnowledgeId(@Param("knowledgeId") int knowledgeId);
}
