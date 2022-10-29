package pers.juumii.MindTrace.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.mapper.utils.DataMapper;

import java.util.List;

public interface KnowledgeMapper extends DataMapper<Knowledge> {
    List<Knowledge> queryBySuperKnowledgeId(@Param("superKnowledgeId") int id);
}
