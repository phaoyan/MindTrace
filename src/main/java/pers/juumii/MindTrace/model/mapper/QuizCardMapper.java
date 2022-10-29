package pers.juumii.MindTrace.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.mapper.utils.DataMapper;

import java.util.List;


public interface QuizCardMapper extends DataMapper<QuizCard>{
    List<QuizCard> queryByQuizId(@Param("quizId") int quizId);
}
