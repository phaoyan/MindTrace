package pers.juumii.MindTrace.model.service.quiz.generator;

import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.Repository;

import java.util.Comparator;
import java.util.List;

@Service
public class MasteryBasedQuiz implements RandomQuiz{

    private final Repository repository;

    public MasteryBasedQuiz(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<QuizCard> analyze(int count) {
        List<QuizCard> quizzes = repository.getByType(QuizCard.class);
        assert count <= quizzes.size();
        quizzes.sort(Comparator.comparing(quizCard -> quizCard.getQuizTask(repository).getKnowledge(repository).getMasteryAvg()));
        return quizzes.subList(0, count - 1);
    }
}
