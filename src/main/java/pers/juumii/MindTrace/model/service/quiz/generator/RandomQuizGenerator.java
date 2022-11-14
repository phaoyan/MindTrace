package pers.juumii.MindTrace.model.service.quiz.generator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class RandomQuizGenerator extends QuizGenerator{

    @Autowired
    public RandomQuizGenerator(KTree kTree) {
        super(kTree);
    }

    @Override
    public List<QuizCard> quizzes() {
        List<QuizCard> res = new ArrayList<>();
        List<Integer> indexes = MathUtils.randomIndexes(0, quizRepository.size(), scale);
        indexes.forEach(index -> res.add(quizRepository.get(index)));
        return res;
    }
}
