package pers.juumii.MindTrace.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.juumii.MindTrace.model.data.LearningCard;
import pers.juumii.MindTrace.model.mapper.utils.DataMapper;

import java.util.List;

public interface LearningCardMapper extends DataMapper<LearningCard> {
    List<LearningCard> queryByKnowledgeId(@Param("knowledgeId") int knowledgeId);
}
