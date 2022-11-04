package pers.juumii.MindTrace.model.service;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.LearningCard;
import pers.juumii.MindTrace.model.data.QuizCard;

import java.util.List;


class LinkingSearcherTest {

    private final Repository repository;
    private final LinkingSearcher searcher;

    public LinkingSearcherTest(){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        repository = ctx.getBean(Repository.class);
        searcher = ctx.getBean(LinkingSearcher.class);
    }

    @Test
    void getKnowledgesBeyond(){
        List<Knowledge> knowledgesBeyond = searcher.getKnowledgesBeyond(repository.getById(29, Knowledge.class));
        for(Knowledge knowledge: knowledgesBeyond)
            System.out.println(knowledge);
    }

    @Test
    void getKnowledgesBeneath(){
        List<Knowledge> knowledges = searcher.getKnowledgesBeneath(repository.getById(2, Knowledge.class));
        for(Knowledge knowledge: knowledges)
            System.out.println(knowledge);
    }

    @Test
    void getKnowledgesAside(){
        List<Knowledge> knowledges = searcher.getKnowledgesAside(repository.getById(13, Knowledge.class));
        for(Knowledge knowledge: knowledges)
            System.out.println(knowledge);
    }


    @Test
    void getQuizTasksAside(){
        List<QuizCard> quizCards = searcher.getQuizCardsAside(repository.getById(125, QuizCard.class));
        quizCards.forEach(System.out::println);
    }


    @Test
    void getQuizTasksBeneath(){
        List<QuizCard> quizCards = searcher.getQuizCardsBeneath(repository.getById(125, QuizCard.class));
        quizCards.forEach(System.out::println);
    }
}