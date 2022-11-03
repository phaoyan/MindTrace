package pers.juumii.MindTrace.view.console;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.*;
import pers.juumii.MindTrace.model.service.GeneralStatistics;
import pers.juumii.MindTrace.model.service.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@ToString
public class WelcomeUI {
    @Autowired
    private Repository repository;
    private int knowledgeCount;
    private int learningTaskCount;
    private int quizTaskCount;

    @PostConstruct
    public void init(){
        knowledgeCount = repository.getByType(Knowledge.class).size();
        learningTaskCount = repository.getByType(LearningTask.class).size();
        quizTaskCount = repository.getByType(QuizTask.class).size();

    }
    public void welcome(){
        System.out.printf("""
            Welcome!
            Knowledge / Learning Tasks / Quiz Tasks Recorded: %s / %s / %s
            """,
            GeneralStatistics.getItemCount(Knowledge.class),
            GeneralStatistics.getItemCount(LearningTask.class),
            GeneralStatistics.getItemCount(QuizTask.class));
        List<Knowledge> urges = GeneralStatistics.getKnowledgesBelowLevelLine();
        if(urges.size() != 0){
            System.out.println(urges.size() + " Knowledges have Mastery level: LOWER, and it might be an urge to review:");
            urges.forEach(urge -> System.out.println(">>> "+urge.getDesc() + "[" + urge.getMasteryAvg() + "]"));
            System.out.println("enter  query <knowledge title>  to review");
        }
    }

    private void handleOption(String option) {

    }

    public static void main(String[] args) {

    }
}
