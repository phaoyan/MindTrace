package pers.juumii.MindTrace.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class Statistics {


    private final KTree kTree;

    @Autowired
    public Statistics(KTree kTree) {
        this.kTree = kTree;
    }

    public LocalDateTime lastQuizTime(){
        List<QuizCard> quizCards = kTree.getQuizCards();
        //将quizCard按照时间升序排序
        quizCards.sort(Comparator.comparing((card)-> card.getQuizRecords().isEmpty() ? LocalDateTime.now().minusYears(999): card.getQuizRecords().get(card.getQuizRecords().size()-1).getTime()));
        //最后一张quizCard的最后一个quizRecord的时间即上次quizTask的时间
        return quizCards.get(quizCards.size()-1).getQuizRecords().isEmpty() ? LocalDateTime.now().minusYears(999): quizCards.get(quizCards.size()-1).getQuizRecords().get(quizCards.get(quizCards.size()-1).getQuizRecords().size()-1).getTime();
    }
}
