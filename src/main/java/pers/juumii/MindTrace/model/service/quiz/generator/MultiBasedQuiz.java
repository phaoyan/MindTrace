package pers.juumii.MindTrace.model.service.quiz.generator;

import lombok.Setter;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.data.QuizRecord;
import pers.juumii.MindTrace.model.service.GeneralStatistics;
import pers.juumii.MindTrace.model.service.LinkingSearcher;
import pers.juumii.MindTrace.model.service.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 对Record、Mastery基准分别进行一定的加权，权值相加得到排序结果
 */
@Service
public class MultiBasedQuiz implements RandomQuiz{
    private final Repository repository;
    private final LinkingSearcher searcher;
    private final LocalDateTime avgRecordTime;
    private final Duration timeScale;
    @Setter
    private double recordWeight;
    @Setter
    private double masteryWeight;

    public MultiBasedQuiz(Repository repository, LinkingSearcher searcher) {
        this.repository = repository;
        this.searcher = searcher;
        this.avgRecordTime = GeneralStatistics.getAvgLastRecordTime();
        this.timeScale = GeneralStatistics.getTimeScale();
    }


    @Override
    public List<QuizCard> analyze(int count) {
        List<QuizCard> quizzes = repository.getByType(QuizCard.class);
        assert count <= quizzes.size();
        quizzes.sort(Comparator.comparing(this::weight));
        return quizzes.subList(0, count - 1);
    }

    private double weight(QuizCard quizCard) {
        List<QuizRecord> quizHistory = searcher.getQuizRecord(quizCard.getQuizTask(repository));
        LocalDateTime lastRecordTime = quizHistory.get(quizHistory.size() - 1).getTime();
        double recordScore = (double) Duration.between(lastRecordTime, avgRecordTime).getNano() / timeScale.getNano() * recordWeight;
        double masteryScore = quizCard.getQuizTask(repository).getKnowledge(repository).getMasteryAvg() * masteryWeight;
        return recordScore + masteryScore;
    }
}
