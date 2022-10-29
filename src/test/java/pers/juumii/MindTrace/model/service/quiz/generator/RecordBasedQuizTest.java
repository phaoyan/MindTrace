package pers.juumii.MindTrace.model.service.quiz.generator;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.data.QuizTask;
import pers.juumii.MindTrace.model.service.Repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecordBasedQuizTest {

    @Test
    void analyze() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        Repository repository = ctx.getBean(Repository.class);
        List<QuizCard> quizCards = ctx.getBean(RecordBasedQuiz.class).analyze(100);
        for(QuizCard quizCard: quizCards){
            QuizTask quizTask = repository.getById(quizCard.getQuizId(), QuizTask.class);
            Knowledge knowledge = repository.getById(quizTask.getKnowledgeId(), Knowledge.class);
        }
    }
}