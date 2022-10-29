package pers.juumii.MindTrace.model.service.quiz.generator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import pers.juumii.MindTrace.MindTraceApplication;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PureRandomQuizTest {

    public static void main(String[] args) {
        SpringApplication.run(MindTraceApplication.class, args);
        List<QuizCard> analyze = SpringUtils.getBean(PureRandomQuiz.class).analyze(10);
        for(QuizCard quizCard: analyze)
            System.out.println(quizCard);
    }

}