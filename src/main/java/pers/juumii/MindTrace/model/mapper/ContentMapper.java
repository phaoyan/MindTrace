package pers.juumii.MindTrace.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.juumii.MindTrace.model.data.Content;
import pers.juumii.MindTrace.model.mapper.utils.DataMapper;

import java.util.List;

public interface ContentMapper extends DataMapper<Content> {

    int updateTaskId(@Param("taskId") int taskId, @Param("id") int id);
    List<Content> queryByTaskId(@Param("taskId") int taskId);
}
