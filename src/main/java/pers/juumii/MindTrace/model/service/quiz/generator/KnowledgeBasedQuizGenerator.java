package pers.juumii.MindTrace.model.service.quiz.generator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KNode;
import pers.juumii.MindTrace.model.service.ktree.KTree;

import java.util.ArrayList;
import java.util.List;

@Service
public class KnowledgeBasedQuizGenerator extends QuizGenerator{

    @Setter
    @Getter
    private KNode range;

    @Autowired
    public KnowledgeBasedQuizGenerator(KTree kTree) {
        super(kTree);
    }

    @Override
    public List<QuizCard> quizzes() {
        List<QuizCard> res = new ArrayList<>();
        quizRepository.forEach(quizCard ->
            res.addAll(range.querySubKNode(quizCard.getKnowledgeId()).getData().getQuizCards()));
        return res;
    }
}
