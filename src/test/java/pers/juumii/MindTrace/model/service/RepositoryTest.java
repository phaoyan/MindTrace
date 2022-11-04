package pers.juumii.MindTrace.model.service;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.model.data.*;
import pers.juumii.MindTrace.utils.MathUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

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
        for(int i = 1; i < 30; i ++){
            Knowledge knowledge = repo.create(Knowledge.class);
            knowledge.setDescription("Knowledge: "+ MathUtils.getRandomString(10));
            knowledge.setId(repo.getByType(Knowledge.class).size());
            if(i > 3)
                knowledge.setSuperKnowledgeId(new Random().nextInt(0,i-1));
            knowledge.setMasteryMax(new Random().nextInt(1,100));
            knowledge.setMasteryMin(new Random().nextInt(0,knowledge.getMasteryMax()));

            int learningCardCount = new Random().nextInt(0, 10);
            for(int j = 0; j < learningCardCount; j ++){
                LearningCard learningCard = repo.create(LearningCard.class);
                learningCard.setDescription("LearningCard: "+MathUtils.getRandomString(10));
                learningCard.setId(repo.getByType(LearningCard.class).size());
                learningCard.setKnowledgeId(knowledge.getId());
                learningCard.setResource("LearningResource: "+MathUtils.getRandomString(10));

                int recordCount = new Random().nextInt(0, 10);
                for(int k = 0; k < recordCount; k ++){
                    LearningRecord learningRecord = repo.create(LearningRecord.class);
                    learningRecord.setId(repo.getByType(LearningRecord.class).size());
                    learningRecord.setCompletion(new Random().nextInt(0,100));
                    learningRecord.setTime(LocalDateTime.now().plus(Duration.ofNanos(new Random().nextLong(0,10000000))));
                    learningRecord.setDescription("LearningRecord: "+MathUtils.getRandomString(10));
                    learningRecord.setCardId(learningCard.getId());
                }
            }

            int quizCardCount = new Random().nextInt(0,10);
            for(int j = 0; j < quizCardCount; j ++){
                QuizCard quizCard = repo.create(QuizCard.class);
                quizCard.setDescription("QuizCard: "+MathUtils.getRandomString(10));
                quizCard.setId(repo.getByType(QuizCard.class).size()+1);
                quizCard.setKnowledgeId(knowledge.getId());
                quizCard.setFront("Front: "+MathUtils.getRandomString(10));
                quizCard.setBack("Back: "+MathUtils.getRandomString(10));

                int recordCount = new Random().nextInt(0, 10);
                for(int k = 0; k < recordCount; k ++){
                    QuizRecord quizRecord = repo.create(QuizRecord.class);
                    quizRecord.setId(repo.getByType(QuizRecord.class).size()+1);
                    quizRecord.setCompletion(new Random().nextInt(0,100));
                    quizRecord.setTime(LocalDateTime.now().plus(Duration.ofNanos(new Random().nextLong(0,10000000))));
                    quizRecord.setDescription("QuizRecord: "+MathUtils.getRandomString(10));
                    quizRecord.setCardId(quizCard.getId());
                }
            }
        }

        repo.commit();
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
        repo.getByType(Knowledge.class).forEach(System.out::println);
        repo.getByType(LearningRecord.class).forEach(System.out::println);
        repo.getByType(QuizRecord.class).forEach(System.out::println);
        repo.getByType(LearningCard.class).forEach(System.out::println);
        repo.getByType(QuizCard.class).forEach(System.out::println);
    }

    @Test
    void remove(){
        repo.load();
        repo.commit();
    }
}