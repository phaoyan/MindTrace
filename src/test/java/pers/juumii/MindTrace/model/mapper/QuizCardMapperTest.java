package pers.juumii.MindTrace.model.mapper;

import org.junit.jupiter.api.Test;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.data.QuizRecord;
import pers.juumii.MindTrace.utils.SqlSessionUtils;

import java.time.LocalDateTime;

class QuizCardMapperTest {

    public static final QuizCardMapper mapper = SqlSessionUtils.getSqlSession().getMapper(QuizCardMapper.class);
    @Test
    void insertQuizTask() {
        QuizCard quizCard = new QuizCard();
        quizCard.setId(10086);
        quizCard.setKnowledgeId(777);
        QuizRecordMapper quizRecordMapper = SqlSessionUtils.getSqlSession().getMapper(QuizRecordMapper.class);
        QuizRecord quizRecord = new QuizRecord();
        quizRecord.setTime(LocalDateTime.now());
        quizRecord.setCardId(quizCard.getId());
        quizRecord.setDescription("quiz record...");
        quizRecordMapper.insert(quizRecord);
        mapper.insert(quizCard);
    }

    @Test
    void queryTasksByKnowledgeId() {
        System.out.println(mapper.queryById(10086));
    }

    @Test
    void queryTasksById() {
    }
}