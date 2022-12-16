package pers.juumii.MindTrace.model.service.ktree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.InstantData;
import pers.juumii.MindTrace.model.data.LearningRecord;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.data.QuizRecord;
import pers.juumii.MindTrace.utils.Constants;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Data
@Service
@InstantData
public class KTree {

    @InstantData
    @JsonIgnore
    private KTreeLoader loader;
    private KNode root;
    private KTreeConfigs configs;


    @Autowired
    public KTree(@Qualifier("KTreeJsonLoader") KTreeLoader loader) {
        this.loader = loader;
        load();
    }

    public KTree(){}

    public void load() {
        loader.load(this);
    }

    //将KTree中储存的数据同步到repository
    public void synchronize(){
        loader.synchronize(this);
    }

    public List<QuizCard> queryQuizCards(){
        List<QuizCard> res = new ArrayList<>();
        root.getKNodesBeneath().forEach(kNode -> res.addAll(kNode.getData().getQuizCards()));
        return res;
    }

    @JsonIgnore
    public KNode getKNode(long id){
        return id == 0 ? root: DataUtils.getIf(root.getKNodesBeneath(), kNode->kNode.getData().getId() == id);
    }



    @InstantData
    public List<LearningRecord> getLearningRecordsToday(){
        return DataUtils.getAllIf(configs.getLearningRecords(), record->record.getStartTime().isAfter(Constants.today));
    }

    @InstantData
    public Duration getLearningTimeSpent(){
        return Duration.ofSeconds(configs.getLearningRecords().stream().mapToLong(record->record.getDuration().toSeconds()).sum());
    }

    @InstantData
    public Duration getLearningTimeSpentToday(){
        return Duration.ofSeconds(getLearningRecordsToday().stream().mapToLong(record -> record.getDuration().toSeconds()).sum());
    }

    @InstantData
    public List<KNode> getNewlyAddedKNodes(){
        return DataUtils.getAllIf(root.getKNodesBeneath(), kNode -> kNode.getData().getEstablishTime() != null && kNode.getData().getEstablishTime().isAfter(Constants.today));
    }

    @InstantData
    public List<QuizCard> getNewlyAddedQuizCards(){
        return DataUtils.getAllIf(root.getQuizCardsBeneath(), card->card.getEstablishTime().isAfter(Constants.today));
    }

    @InstantData
    public int getNewlyAddedQuizCardScale(){
        return getNewlyAddedQuizCards().stream().mapToInt(QuizCard::getScale).sum();
    }

    @InstantData
    public List<QuizCard> getNewlyReviewedQuizCards(){
        return DataUtils.getAllIf(root.getQuizCardsBeneath(), card-> !card.getQuizRecords().isEmpty() && card.getQuizRecords().get(card.getQuizRecords().size()-1).getTime().isAfter(Constants.today));
    }

    @InstantData
    public List<QuizCard> getQuizCardsSortedByEstablishment(){
        List<QuizCard> res = root.getQuizCardsBeneath();
        res.sort(Comparator.comparing(QuizCard::getEstablishTime));
        return DataUtils.reverse(res);
    }

    @InstantData
    public LocalDateTime getLastQuizTime(){
        List<List<QuizRecord>> recordGroups = DataUtils.destructureAll(root.getQuizCardsBeneath(), QuizCard::getQuizRecords);
        if(recordGroups.isEmpty())
            return Constants.timeAnchor;
        recordGroups.sort(Comparator.comparing(recordGroup -> recordGroup.isEmpty() ? Constants.timeAnchor : recordGroup.get(recordGroup.size()-1).getTime()));
        return DataUtils.getLast(recordGroups.get(recordGroups.size() - 1)) == null ? Constants.timeAnchor : DataUtils.getLast(recordGroups.get(recordGroups.size() - 1)).getTime();
    }

    @InstantData
    public Map<LocalDate, List<LearningRecord>> getLearningRecordsGroupedByDate(){
        return DataUtils.groupBy(configs.getLearningRecords(), record -> record.getStartTime().toLocalDate());
    }

    @InstantData
    public Map<LocalDate, String> getLearningOverviews(){
        Map<LocalDate, String> res = new HashMap<>();
        getLearningRecordsGroupedByDate().forEach((date, records) ->{
            Duration learningTime = Duration.ofSeconds(records.stream().mapToLong(record -> record.getDuration().toSeconds()).sum());
            int newKNodeCount = DataUtils.getAllIf(root.getKNodesBeneath(), kNode -> kNode.getData().getEstablishTime().toLocalDate().equals(date)).size();
            int newQuizCardCount = DataUtils.getAllIf(root.getQuizCardsBeneath(), card -> card.getEstablishTime().toLocalDate().equals(date)).size();
            int reviewedQuizCardCount = DataUtils.getAllIf(root.getQuizCardsBeneath(), card->DataUtils.getIf(card.getQuizRecords(), record->record.getTime().toLocalDate().equals(date)) != null).size();
            res.put(date, """
                 # %s
                 - *%d* hours *%d* minutes *%d* seconds spent on recorded learning today.
                 - *%d* KNodes newly added.
                 - *%d* quiz cards newly added.
                 - *%d* quiz cards reviewed.
                """.formatted(
                        date.toString(),
                        learningTime.toHoursPart(), learningTime.toMinutesPart(), learningTime.toSecondsPart(),
                        newKNodeCount,
                        newQuizCardCount,
                        reviewedQuizCardCount
            ));
        });
        return res;
    }

    @InstantData
    public String getOverviewMarkdown(){
        return """
                # *General Record*
                 - *%d* hours *%d* minutes *%d* seconds spent on this subject.
                 - *%d* KNodes registered.
                 - *%d* quiz cards registered. (*%d* hours *%d* minutes needed to fully review)
                 - *%d* quiz cards are never reviewed. (*%d* hours *%d* minutes needed to fully review)
                # *Today*
                 - *%d* hours *%d* minutes *%d* seconds spent on recorded learning today.
                 - *%d* KNodes newly added.
                 - *%d* quiz cards newly added. (*%d* hours *%d* minutes needed to fully review)
                 - *%d* quiz cards reviewed.
                 
                # *Quiz Task*
                 - *%d*/*%d*/*%d* is the last quiz task time.
                """
                .formatted(
                        getLearningTimeSpent().toHoursPart(), getLearningTimeSpent().toMinutesPart(), getLearningTimeSpent().toSecondsPart(),
                        root.getSize(),
                        root.getQuizCardsBeneath().size(), Duration.ofMinutes(root.getTotalQuizScale()).toHoursPart(), Duration.ofMinutes(root.getTotalQuizScale()).toMinutesPart(),
                        root.getUnreviewedQuizCardsBeneath().size(), Duration.ofMinutes(root.getUnreviewedQuizScale()).toHoursPart(), Duration.ofMinutes(root.getUnreviewedQuizScale()).toMinutesPart(),
                        getLearningTimeSpentToday().toHoursPart(), getLearningTimeSpentToday().toMinutesPart(), getLearningTimeSpentToday().toSecondsPart(),
                        getNewlyAddedKNodes().size(),
                        getNewlyAddedQuizCards().size(), Duration.ofMinutes(getNewlyAddedQuizCardScale()).toHoursPart(),Duration.ofMinutes(getNewlyAddedQuizCardScale()).toMinutesPart(),
                        getNewlyReviewedQuizCards().size(),
                        getLastQuizTime().getYear(), getLastQuizTime().getMonthValue(), getLastQuizTime().getDayOfMonth()
                );
    }



}
