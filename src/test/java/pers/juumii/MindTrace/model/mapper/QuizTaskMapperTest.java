package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.QuizRecord;
import pers.juumii.MindTrace.model.data.QuizTask;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

import java.time.LocalDateTime;

class QuizTaskMapperTest {

    public static final QuizTaskMapper mapper = SqlSessionUtils.getSqlSession().getMapper(QuizTaskMapper.class);
    @Test
    void insertQuizTask() {
        QuizTask quizTask = new QuizTask();
        quizTask.setId(10086);
        quizTask.setKnowledgeId(777);
        QuizRecordMapper quizRecordMapper = SqlSessionUtils.getSqlSession().getMapper(QuizRecordMapper.class);
        QuizRecord quizRecord = new QuizRecord();
        quizRecord.setTime(LocalDateTime.now());
        quizRecord.setTaskId(quizTask.getId());
        quizRecord.setDesc("quiz record...");
        quizRecordMapper.insert(quizRecord);
        mapper.insert(quizTask);
    }

    @Test
    void queryTasksByKnowledgeId() {
        System.out.println(mapper.queryById(10086));
    }

    @Test
    void queryTasksById() {
    }
}