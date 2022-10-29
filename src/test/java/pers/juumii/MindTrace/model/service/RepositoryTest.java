package pers.juumii.MindTrace.model.service;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.model.data.*;
import pers.juumii.MindTrace.utils.MathUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {

    public Repository repo;
    public LinkingSearcher searcher;

    public RepositoryTest(){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        repo = ctx.getBean(Repository.class);
        searcher = ctx.getBean(LinkingSearcher.class);
    }


    @Test
    void deleteAll(){
        repo.load();
        for(Persistent data: repo.getAll())
            data.clear();
        repo.commit();
    }

    @Test
    void generateDataTopDown(){
        repo.load();
        List<Knowledge> knowledges = new ArrayList<>();
        int knowledgeId = 1;
        int learningTaskId = 1;
        int learningContentId = 1;
        int learningRecordId = 1;
        int quizTaskId = 1;
        int quizCardId = 1;
        int quizRecordId = 1;
        for(int i = 1; i <= 30; i ++){
            Knowledge knowledge = repo.create(Knowledge.class);
            knowledge.setId(knowledgeId++);
            knowledge.setDesc("Knowledge: " + MathUtils.getRandomString(10));
            if(knowledges.size() > 1 && new Random().nextInt(1,10) >= 7)
                knowledges.get(new Random().nextInt(0,knowledges.size()-1)).setSuperKnowledgeId(knowledge.getId());

            int learningTaskCount = new Random().nextInt(0, 10);
            for(int i0 = 0; i0 < learningTaskCount; i0 ++){
                LearningTask learningTask = repo.create(LearningTask.class);
                learningTask.setId(learningTaskId++);
                learningTask.setDesc("Learning Task: " + MathUtils.getRandomString(10));
                int contentCount = new Random().nextInt(0,10);
                for(int i1 = 0; i1 < contentCount; i1 ++){
                    Content content = repo.create(Content.class);
                    content.setId(learningContentId++);
                    content.setDesc("Content: " + MathUtils.getRandomString(10));
                    content.setSrc("Content Resources: " + MathUtils.getRandomString(10));
                    content.setTaskId(learningTask.getId());
                }
                int recordCount = new Random().nextInt(0,10);
                for(int i1 = 0; i1 < recordCount; i1 ++){
                    LearningRecord record = repo.create(LearningRecord.class);
                    record.setId(learningRecordId++);
                    record.setDesc("Learning Record:" + MathUtils.getRandomString(10));
                    record.setCompletion(new Random().nextInt(0,100));
                    record.setTime(LocalDateTime.now().plus(Duration.ofMinutes(new Random().nextInt(-10000,10000))));
                    record.setTaskId(learningTask.getId());
                }
                if(i0 > 3) {
                    List<Integer> indexes = MathUtils.randomIndexes(0, repo.getByType(LearningTask.class).size() - 1, 3);
                    for (int index : indexes)
                        repo.getByType(LearningTask.class).get(index).setSuperTaskId(learningTask.getId());
                }
                learningTask.setKnowledgeId(knowledge.getId());
            }

            int quizTaskCount = new Random().nextInt(0, 10);
            for(int i0 = 0; i0 < quizTaskCount; i0 ++){
                QuizTask quizTask = repo.create(QuizTask.class);
                quizTask.setId(quizTaskId++);
                quizTask.setDesc("Quiz Task: " + MathUtils.getRandomString(10));
                int cardCount = new Random().nextInt(0,10);
                for(int i1 = 0; i1 < cardCount; i1 ++){
                    QuizCard quizCard = repo.create(QuizCard.class);
                    quizCard.setId(quizCardId++);
                    quizCard.setDesc("Quiz Card: " + MathUtils.getRandomString(10));
                    quizCard.setBack("Back: " + MathUtils.getRandomString(10));
                    quizCard.setFront("Front: " + MathUtils.getRandomString(10));
                    quizCard.setQuizId(quizTask.getId());
                }
                int recordCount = new Random().nextInt(0,10);
                for(int i1 = 0; i1 < recordCount; i1 ++){
                    QuizRecord quizRecord = repo.create(QuizRecord.class);
                    quizRecord.setId(quizRecordId++);
                    quizRecord.setDesc("Quiz Record: " + MathUtils.getRandomString(10));
                    quizRecord.setCompletion(new Random().nextInt(0,100));
                    quizRecord.setTime(LocalDateTime.now().plus(Duration.ofMinutes(new Random().nextInt(-10000,10000))));
                    quizRecord.setTaskId(quizTask.getId());
                }
                quizTask.setKnowledgeId(knowledge.getId());
            }

            knowledges.add(knowledge);
        }
        repo.commit();
//        System.out.println();
    }

    @Test
    void commit() {
        repo.load();
        repo.commit();
    }

    @Test
    void load() {
        repo.load();
        System.out.println(repo);
    }

    @Test
    void update(){
        repo.load();
        Knowledge knowledge = repo.getById(66, Knowledge.class);
        repo.commit();
    }

    @Test
    void getByType(){
        repo.load();
        repo.getByType(Content.class).forEach(System.out::println);
        repo.getByType(Knowledge.class).forEach(System.out::println);
        repo.getByType(LearningRecord.class).forEach(System.out::println);
        repo.getByType(QuizRecord.class).forEach(System.out::println);
        repo.getByType(LearningTask.class).forEach(System.out::println);
        repo.getByType(QuizTask.class).forEach(System.out::println);
    }

    @Test
    void remove(){
        repo.load();
        repo.removeById(1, Content.class);
        repo.commit();
    }
}