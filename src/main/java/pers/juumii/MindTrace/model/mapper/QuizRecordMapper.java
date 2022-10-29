package pers.juumii.MindTrace.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.juumii.MindTrace.model.data.QuizRecord;
import pers.juumii.MindTrace.model.mapper.utils.DataMapper;

import java.util.List;

public interface QuizRecordMapper extends DataMapper<QuizRecord> {
    List<QuizRecord> queryByTaskId(@Param("taskId") int taskId);
}
