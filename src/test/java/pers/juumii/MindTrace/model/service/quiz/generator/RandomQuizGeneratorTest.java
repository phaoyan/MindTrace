package pers.juumii.MindTrace.model.service.quiz.generator;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;

import static org.junit.jupiter.api.Assertions.*;

class RandomQuizGeneratorTest {

    @Test
    void quizzes() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        RandomQuizGenerator quizGenerator = ctx.getBean(RandomQuizGenerator.class);
        quizGenerator.setScale(3);
        System.out.println(quizGenerator.quizzes());
    }
}