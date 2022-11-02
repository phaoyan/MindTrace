package pers.juumii.MindTrace.model.service.quiz.generator;

import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PureRandomQuiz implements RandomQuiz{

    private final Repository repository;

    public PureRandomQuiz(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<QuizCard> analyze(int count) {
        List<QuizCard> quizzes = repository.getByType(QuizCard.class);
        List<QuizCard> res = new ArrayList<>();
        List<Integer> indexes = MathUtils.randomIndexes(0, quizzes.size(), count);
        for(int index: indexes)
            res.add(quizzes.get(index));
        return res;
    }
}
