package pers.juumii.MindTrace.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.juumii.MindTrace.model.data.LearningRecord;
import pers.juumii.MindTrace.model.mapper.utils.DataMapper;

import java.util.List;

public interface LearningRecordMapper  extends DataMapper<LearningRecord> {

    List<LearningRecord> queryByTaskId(@Param("taskId") int taskId);
}
