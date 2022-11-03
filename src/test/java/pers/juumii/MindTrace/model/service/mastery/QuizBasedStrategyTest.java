package pers.juumii.MindTrace.model.service.mastery;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.exception.DataClearedException;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.QuizRecord;
import pers.juumii.MindTrace.model.service.LinkingSearcher;
import pers.juumii.MindTrace.model.service.Repository;

import java.util.List;

class QuizBasedStrategyTest {

    @Test
    void evaluate() throws DataClearedException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        Repository repository = ctx.getBean(Repository.class);
        Knowledge knowledge = repository.getById(1, Knowledge.class);
        new QuizBasedStrategy(10).evaluate(knowledge);
        System.out.println("top: "+knowledge.getMasteryMax());
        System.out.println("bottom: "+knowledge.getMasteryMin());

    }
}