package pers.juumii.MindTrace.model.service.quiz.generator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.general.Settings;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public abstract class QuizGenerator {

    @Getter
    @Setter
    protected int scale;
    protected final KTree kTree;
    @Autowired
    public QuizGenerator(KTree kTree) {
        this.kTree = kTree;
    }

    public abstract List<QuizCard> quizzes();

    public static QuizGenerator load(List<QuizGenerator> quizGenerators, Settings settings){
        return switch ((String)settings.query("quizGenerator")){
            case "RandomQuizGenerator" -> DataUtils.getIf(quizGenerators, quizGenerator -> quizGenerator instanceof RandomQuizGenerator);
            case "KnowledgeBasedQuizGenerator" -> DataUtils.getIf(quizGenerators, quizGenerator -> quizGenerator instanceof KnowledgeBasedQuizGenerator);
            case "RecordBasedQuizGenerator" -> DataUtils.getIf(quizGenerators, quizGenerator -> quizGenerator instanceof RecordBasedQuizGenerator);
            default -> quizGenerators.get(0);
        };
    }
}
