package pers.juumii.MindTrace.model.service.ktree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import pers.juumii.MindTrace.model.data.InstantData;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.QuizCard;
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

    private List<KNode> getKNodesBeneath(KNode root){
        List<KNode> res = new ArrayList<>(root.getSubKNodes());
        root.getSubKNodes().forEach(subXNode -> res.addAll(getKNodesBeneath(subXNode)));
        res.add(this);
        return res;
    }

    public KNode querySubKNode(int id){
        return DataUtils.getIf(getKNodesBeneath(), kNode -> kNode.getData().getId() == id);
    }


    @InstantData
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

    @InstantData
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

    @InstantData
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
    public double getAverageQuizCardCompletion(){
        return getKNodesBeneath().stream().mapToDouble(kNode->kNode.getData().getAverageQuizCardCompletion()).sum() / getKNodesBeneath().size();
    }

    @InstantData
    private List<QuizCard> getPerfectRecordQuizCards() {
        return DataUtils.getAllIf(getQuizCardsBeneath(), card-> !card.getQuizRecords().isEmpty() && card.getQuizRecords().get(card.getQuizRecords().size()-1).getCompletion() >= 90);
    }

    @InstantData
    private List<QuizCard> getPoorRecordQuizCards() {
        return DataUtils.getAllIf(getQuizCardsBeneath(), card-> !card.getQuizRecords().isEmpty() && card.getQuizRecords().get(card.getQuizRecords().size()-1).getCompletion() <= 30);
    }

    @InstantData
    private double getRate(){
        return getQuizCardsBeneath().stream().mapToDouble(QuizCard::getRate).sum() / getQuizCardsBeneath().size();
    }

    @InstantData
    public String getOverviewMarkdown(){
        return """
                # Quiz
                 - *%d* quiz cards registered.
                 - *%d* minutes expected to review all quiz cards.
                # Review
                 - *%d* quiz cards reviewed.
                 - *%d* quiz cards never reviewed.
                 - *%d* hours *%d* minutes spent on reviewing
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
