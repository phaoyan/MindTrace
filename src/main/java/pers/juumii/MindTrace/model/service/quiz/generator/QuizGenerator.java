package pers.juumii.MindTrace.model.service.quiz.generator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;

import java.util.List;

@Service
public abstract class QuizGenerator {

    @Getter
    @Setter
    protected int scale;
    protected final KTree kTree;
    protected final List<QuizCard> quizRepository;

    @Autowired
    public QuizGenerator(KTree kTree) {
        this.kTree = kTree;
        this.quizRepository = kTree.getQuizCards();
    }

    public abstract List<QuizCard> quizzes();
}
