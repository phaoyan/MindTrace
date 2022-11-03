package pers.juumii.MindTrace.model.service;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.exception.DataClearedException;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.LearningTask;
import pers.juumii.MindTrace.model.data.QuizTask;

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
    void getKnowledgeChain() throws DataClearedException {
        List<Knowledge> knowledgeChain = searcher.getKnowledgeChain(repository.getById(13, Knowledge.class));
        for(Knowledge knowledge: knowledgeChain)
            System.out.println(knowledge);
    }

    @Test
    void getAllKnowledgesBeyond() throws DataClearedException {
        List<Knowledge> knowledges = searcher.getAllKnowledgesBeyond(repository.getById(13, Knowledge.class));
        for(Knowledge knowledge: knowledges)
            System.out.println(knowledge);
    }

    @Test
    void getAllKnowledgesBeneath() throws DataClearedException {
        List<Knowledge> knowledges = searcher.getAllKnowledgesBeneath(repository.getById(27, Knowledge.class));
        for(Knowledge knowledge: knowledges)
            System.out.println(knowledge);
    }

    @Test
    void getKnowledgesAside() throws DataClearedException {
        List<Knowledge> knowledges = searcher.getKnowledgesAside(repository.getById(13, Knowledge.class));
        for(Knowledge knowledge: knowledges)
            System.out.println(knowledge);
    }

    @Test
    void getLearningTaskChain() throws DataClearedException {
        List<LearningTask> learningTaskChain = searcher.getLearningTaskChain(repository.getById(10, LearningTask.class));
        for(LearningTask task: learningTaskChain)
            System.out.println(task);
    }

    @Test
    void getQuizTasksAside() throws DataClearedException {
        List<QuizTask> quizTasks = searcher.getQuizTasksAside(repository.getById(125, QuizTask.class));
        quizTasks.forEach(System.out::println);
    }

    @Test
    void getAllQuizTasksAside() throws DataClearedException {
        List<QuizTask> quizTasks = searcher.getAllQuizTasksAside(repository.getById(125, QuizTask.class));
        quizTasks.forEach(System.out::println);
    }

    @Test
    void getQuizTasksBeneath() throws DataClearedException {
        List<QuizTask> quizTasks = searcher.getQuizTasksBeneath(repository.getById(125, QuizTask.class));
        quizTasks.forEach(System.out::println);
    }
}