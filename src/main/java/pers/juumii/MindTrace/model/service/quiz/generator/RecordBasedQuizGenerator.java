package pers.juumii.MindTrace.model.service.quiz.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class RecordBasedQuizGenerator extends QuizGenerator{

    @Autowired
    public RecordBasedQuizGenerator(KTree kTree) {
        super(kTree);
        quizRepository.sort(Comparator.comparingInt(card ->
            card.getQuizRecords().isEmpty() ? new Random().nextInt(-10000,0):
            card.getQuizRecords().get(card.getQuizRecords().size() - 1).getTime().getNano()));
    }

    @Override
    public List<QuizCard> quizzes() {
        return quizRepository.subList(0, scale - 1);
    }
}
