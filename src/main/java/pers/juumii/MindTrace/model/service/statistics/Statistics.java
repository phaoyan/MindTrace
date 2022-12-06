package pers.juumii.MindTrace.model.service.statistics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KNode;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.Constants;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

@Service
public class Statistics {


    @Getter
    @Setter
    private KTree kTree;

    @Autowired
    public Statistics(KTree kTree) {
        this.kTree = kTree;
    }

    public int kNodeCount(){
        return kTree.size();
    }

    public int quizCardCount(){
        return kTree.quizCardSize();
    }

    public int learningCardCount(){
        return kTree.learningCardSize();
    }

    //返回所有quizCard的scale总和
    public int totalScale(){
        return kTree.queryQuizCards().stream().mapToInt(QuizCard::getScale).sum();
    }

    //返回所有没有复习记录的quizCard的scale总和
    public int totalScaleOfUnreviewed(){
        return DataUtils.getAllIf(kTree.queryQuizCards(), card -> card.getQuizRecords().isEmpty()).stream().mapToInt(QuizCard::getScale).sum();
    }

    public int quizCardCountOfUnreviewed(){
        return DataUtils.getAllIf(kTree.queryQuizCards(), quizCard -> quizCard.getQuizRecords().isEmpty()).size();
    }

    public int totalScaleOfNewlyAddedQuizCards(){
        return newlyAddedQuizCards().stream().mapToInt(QuizCard::getScale).sum();
    }

    public int completedQuizCardScale(){
        int res = 0;
        for(QuizCard completed: completedQuizCards())
            res += completed.getScale();
        return res;
    }

    public List<KNode> newlyAddedKNodes(){
        return DataUtils.getAllIf(kTree.getRoot().queryKNodeBeneath(), kNode -> kNode.getData().getEstablishTime() != null && kNode.getData().getEstablishTime().isAfter(Constants.today));
    }

    //返回当天已经做过的quizCard
    public List<QuizCard> completedQuizCards(){
        return DataUtils.getAllIf(kTree.queryQuizCards(), quizCard -> DataUtils.getLast(quizCard.getQuizRecords()) != null && DataUtils.getLast(quizCard.getQuizRecords()).getTime().isAfter(LocalDateTime.now().minusDays(1)));
    }


    //返回当天新增的quizCards
    public List<QuizCard> newlyAddedQuizCards(){
        return DataUtils.getAllIf(kTree.queryQuizCards(), quizCard -> quizCard.getEstablishTime().isAfter(Constants.today));
    }


    public List<QuizCard> quizCardsSortedByEstablishment(){
        return quizCardsSortedByEstablishment(kTree.queryQuizCards());
    }

    public List<QuizCard> quizCardsSortedByEstablishment(List<QuizCard> quizCards){
        quizCards.sort(Comparator.comparing(QuizCard::getEstablishTime));
        return DataUtils.reverse(quizCards);
    }

    public List<QuizCard> newlyReviewedQuizCards(){
        return DataUtils.getAllIf(kTree.queryQuizCards(), quizCard -> !quizCard.getQuizRecords().isEmpty() && DataUtils.getLast(quizCard.getQuizRecords()).getTime().isAfter(Constants.today));
    }

    public Duration learningTimeRevealedByQuizCards(){
        return learningTimeRevealedByQuizCards(kTree.queryQuizCards());
    }

    public Duration learningTimeRevealedByQuizCards(List<QuizCard> quizCards){
        Duration res = Duration.ZERO;
        Stack<LocalDateTime> establishMoments = new Stack<>();
        quizCardsSortedByEstablishment(quizCards).forEach(card -> establishMoments.push(card.getEstablishTime()));
        if(establishMoments.isEmpty())
            return res;
        LocalDateTime prev = establishMoments.pop();
        while (!establishMoments.isEmpty()){
            LocalDateTime cur = establishMoments.pop();
            Duration interval = Duration.between(prev, cur);
            res = res.plus(interval.compareTo(Constants.ignoreTimeInterval) < 0 ? interval : Duration.ZERO);
            prev = cur;
        }
        return res;
    }

    public LocalDateTime lastQuizTime(){
        List<QuizCard> quizCards = kTree.queryQuizCards();
        if(quizCards.isEmpty())
            return null;
        //将quizCard按照时间升序排序
        quizCards.sort(Comparator.comparing((card)-> card.getQuizRecords().isEmpty() ? LocalDateTime.now().minusYears(999): card.getQuizRecords().get(card.getQuizRecords().size()-1).getTime()));
        //最后一张quizCard的最后一个quizRecord的时间即上次quizTask的时间
        return quizCards.get(quizCards.size()-1).getQuizRecords().isEmpty() ? LocalDateTime.now().minusYears(999): quizCards.get(quizCards.size()-1).getQuizRecords().get(quizCards.get(quizCards.size()-1).getQuizRecords().size()-1).getTime();
    }

    public String overviewMarkdown(){
        return """
                # *General Record*
                 - *%d* hours *%d* minutes *%d* seconds spent on this subject.
                 - *%d* KNodes registered.
                 - *%d* quiz cards registered. (*%d* hours *%d* minutes needed to fully review)
                 - *%d* quiz cards are never reviewed. (*%d* hours *%d* minutes needed to fully review)
                
                # *Today*
                 - *%d* hours *%d* minutes *%d* seconds spent on this subject today.
                 - *%d* KNodes newly added.
                 - *%d* quiz cards newly added. (*%d* hours *%d* minutes needed to fully review)
                 - *%d* quiz cards reviewed.
                 
                # *Quiz Task Schedule*
                 - *%d*/*%d*/*%d* is the last quiz task time.
                """
                .formatted(
                        learningTimeRevealedByQuizCards().toHoursPart(), learningTimeRevealedByQuizCards().toMinutesPart(), learningTimeRevealedByQuizCards().toSecondsPart(),
                        kNodeCount(),
                        quizCardCount(), Duration.ofMinutes(totalScale()).toHoursPart(), Duration.ofMinutes(totalScale()).toMinutesPart(),
                        quizCardCountOfUnreviewed(), Duration.ofMinutes(totalScaleOfUnreviewed()).toHoursPart(), Duration.ofMinutes(totalScaleOfUnreviewed()).toMinutesPart(),
                        learningTimeRevealedByQuizCards(newlyAddedQuizCards()).toHoursPart(), learningTimeRevealedByQuizCards(newlyAddedQuizCards()).toMinutesPart(), learningTimeRevealedByQuizCards(newlyAddedQuizCards()).toSecondsPart(),
                        newlyAddedKNodes().size(),
                        newlyAddedQuizCards().size(), Duration.ofMinutes(totalScaleOfNewlyAddedQuizCards()).toHoursPart(),Duration.ofMinutes(totalScaleOfNewlyAddedQuizCards()).toMinutesPart(),
                        newlyReviewedQuizCards().size(),
                        lastQuizTime().getYear(), lastQuizTime().getMonthValue(), lastQuizTime().getDayOfMonth()
                );
    }

}
