package pers.juumii.MindTrace.model.data;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.model.service.KTree;

import static org.junit.jupiter.api.Assertions.*;

class LearningCardTest {

    @Test
    void protoType() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTree kTree = ctx.getBean(KTree.class);
        System.out.println(kTree.quizCardSize());
    }
}