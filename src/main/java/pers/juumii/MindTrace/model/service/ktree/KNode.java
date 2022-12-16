package pers.juumii.MindTrace.model.service.ktree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import pers.juumii.MindTrace.model.data.InstantData;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.LearningRecord;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.utils.SpringUtils;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@ToString
@InstantData
public class KNode {


    private Knowledge data;
    private List<KNode> subKNodes;

    public KNode(){
        subKNodes = new ArrayList<>();
    }

    @JsonIgnore
    private List<KNode> getKNodesBeneath(KNode root){
        List<KNode> res = new ArrayList<>(root.getSubKNodes());
        root.getSubKNodes().forEach(subXNode -> res.addAll(getKNodesBeneath(subXNode)));
        res.add(this);
        return res;
    }

    public KNode querySubKNode(int id){
        return DataUtils.getIf(getKNodesBeneath(), kNode -> kNode.getData().getId() == id);
    }


    @JsonIgnore
    public List<KNode> getKNodesBeneath(){
        return getKNodesBeneath(this);
    }


    @InstantData
    public int getSize() {
        int res = 1;
        for(KNode kNode: subKNodes)
            res += kNode.getSize();
        return res;
    }

    @JsonIgnore
    public List<QuizCard> getQuizCardsBeneath(){
        List<QuizCard> res = new ArrayList<>();
        getKNodesBeneath().forEach(kNode -> res.addAll(kNode.getData().getQuizCards()));
        return res;
    }

    @InstantData
    public List<Long> getQuizCardIdsBeneath(){
        return DataUtils.destructureAll(getQuizCardsBeneath(), QuizCard::getId);
    }

    @JsonIgnore
    public List<QuizCard> getUnreviewedQuizCardsBeneath(){
        return DataUtils.getAllIf(getQuizCardsBeneath(), card->card.getQuizRecords().isEmpty());
    }

    @InstantData
    public List<Long> getUnreviewedQuizCardIdsBeneath(){
        return DataUtils.destructureAll(getUnreviewedQuizCardsBeneath(), QuizCard::getId);
    }

    @InstantData
    public int getTotalQuizScale(){
        AtomicInteger res = new AtomicInteger();
        getQuizCardsBeneath().forEach(card-> res.addAndGet(card.getScale()));
        return res.get();
    }

    @InstantData
    public int getUnreviewedQuizScale(){
        AtomicInteger res = new AtomicInteger();
        getUnreviewedQuizCardsBeneath().forEach(card-> res.addAndGet(card.getScale()));
        return res.get();
    }

    @InstantData
    public Duration getReviewTimeSpent(){
        Duration res = Duration.ZERO;
        for(KNode kNode: getKNodesBeneath())
            res = res.plus(kNode.data.getReviewTimeSpent());
        return res;
    }

    @InstantData
    private Duration getLearningTimeSpent() {
        Duration res = Duration.ZERO;
        //拿到关联该knowledge的learning record数据
        for(LearningRecord record: DataUtils.getAllIf(SpringUtils.getBean(KTree.class).getConfigs().getLearningRecords(), record->record.getConcernedKnowledgeIds().contains(getData().getId())))
        //认为花费的时间为平均值
            res = res.plus(record.getConcernedKnowledgeIds().isEmpty() ? Duration.ZERO : record.getDuration().dividedBy(record.getConcernedKnowledgeIds().size()));
        return res;
    }

    @InstantData Duration getLearningTimeSpentBeneath(){
        Duration res = getLearningTimeSpent();
        for(KNode kNode: subKNodes)
            res = res.plus(kNode.getLearningTimeSpentBeneath());
        return res;
    }

    @InstantData
    public double getAverageQuizCardCompletion(){
        return getKNodesBeneath().stream().mapToDouble(kNode->kNode.getData().getAverageQuizCardCompletion()).sum() / getKNodesBeneath().size();
    }

    @InstantData
    public List<QuizCard> getPerfectRecordQuizCards() {
        return DataUtils.getAllIf(getQuizCardsBeneath(), card-> !card.getQuizRecords().isEmpty() && card.getQuizRecords().get(card.getQuizRecords().size()-1).getCompletion() >= 90);
    }

    @InstantData
    public List<QuizCard> getPoorRecordQuizCards() {
        return DataUtils.getAllIf(getQuizCardsBeneath(), card-> !card.getQuizRecords().isEmpty() && card.getCompletionLevel() == QuizCard.POOR);
    }

    @InstantData
    public double getRate(){
        return getQuizCardsBeneath().stream().mapToDouble(QuizCard::getRate).sum() / getQuizCardsBeneath().size();
    }

    @InstantData
    public String getOverviewMarkdown(){
        return """
                # Quiz
                 - *%d* quiz cards registered.
                 - *%d* minutes expected to review all quiz cards.
                # Learn and Review
                 - *%d* quiz cards reviewed.
                 - *%d* quiz cards never reviewed.
                 - *%d* hours *%d* minutes spent on reviewing
                 - *%d* hours *%d* minutes spent on learning
                # Mastery
                 - *%.2f* is the average completion of quiz cards.
                 - *%d* quiz cards have perfect completion.
                 - *%d* quiz cards have poor completion.
                # Rating
                 - *%.2f* is the average rating of quiz cards.
                 
                """.formatted(
                getQuizCardsBeneath().size(),
                getTotalQuizScale(),
                getQuizCardsBeneath().size() - getUnreviewedQuizCardsBeneath().size(),
                getUnreviewedQuizCardsBeneath().size(),
                getReviewTimeSpent().toHoursPart(),getReviewTimeSpent().toMinutesPart(),
                getLearningTimeSpentBeneath().toHoursPart(),getLearningTimeSpentBeneath().toMinutesPart(),
                getAverageQuizCardCompletion(),
                getPerfectRecordQuizCards().size(),
                getPoorRecordQuizCards().size(),
                getRate()
        );
    }

    public static KNode prototype() {
        KNode res = new KNode();
        res.setData(Knowledge.protoType());
        res.setSubKNodes(new ArrayList<>());
        return res;
    }

    public static KNode getDefault(){
        KNode root = new KNode();
        Knowledge knowledge = new Knowledge();
        knowledge.setDescription("ROOT");
        root.setData(knowledge);
        return root;
    }
}
