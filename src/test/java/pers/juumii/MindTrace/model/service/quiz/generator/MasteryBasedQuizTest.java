package pers.juumii.MindTrace.model.service.quiz.generator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import pers.juumii.MindTrace.MindTraceApplication;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.exception.DataClearedException;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.data.QuizTask;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MasteryBasedQuizTest {

    @Test
    void analyze() throws DataClearedException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        Repository repository = ctx.getBean(Repository.class);
        List<QuizCard> quizCards = ctx.getBean(MasteryBasedQuiz.class).analyze(100);
        for(QuizCard quizCard: quizCards){
//            System.out.println(quizCard);
            QuizTask quizTask = repository.getById(quizCard.getQuizId(), QuizTask.class);
//            System.out.println(quizTask);
            Knowledge knowledge = repository.getById(quizTask.getKnowledgeId(), Knowledge.class);
            System.out.println(knowledge.getMasteryAvg());
        }


    }
}