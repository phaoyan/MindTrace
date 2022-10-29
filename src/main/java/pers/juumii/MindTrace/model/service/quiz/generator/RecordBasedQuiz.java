package pers.juumii.MindTrace.model.service.quiz.generator;

import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.LinkingSearcher;
import pers.juumii.MindTrace.model.service.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class RecordBasedQuiz implements RandomQuiz{

    private final Repository repository;
    private final LinkingSearcher searcher;

    public RecordBasedQuiz(Repository repository, LinkingSearcher searcher) {
        this.repository = repository;
        this.searcher = searcher;
    }

    @Override
    public List<QuizCard> analyze(int count) {
        List<QuizCard> quizzes = repository.getByType(QuizCard.class);
        assert count <= quizzes.size();
        quizzes.sort(Comparator.comparing(
                quiz -> searcher.getQuizRecord(quiz.getQuizTask(repository)).isEmpty() ?
                        LocalDateTime.now():
                        searcher.getQuizRecord(quiz.getQuizTask(repository)).get(0).getTime()));
        return quizzes.subList(0, count-1);
    }
}
