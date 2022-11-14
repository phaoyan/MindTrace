package pers.juumii.MindTrace.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.ktree.Repository;
import pers.juumii.MindTrace.utils.DataUtils;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class Knowledge implements Linkable{

    private int id, superKnowledgeId;
    private int masteryMin, masteryMax;
    private String description;
    private List<LearningCard> learningCards = new ArrayList<>();
    private List<QuizCard> quizCards = new ArrayList<>();

    public boolean isLike(String keyword) {
        return description.contains(keyword);
    }

    public int getMasteryAvg() {
        return (masteryMax + masteryMin) / 2;
    }

    //组装：连接属于这个knowledge的learningCard与quizCard
    @Override
    public void link(Repository repository){
        setLearningCards(DataUtils.getAllIf(repository.getByType(LearningCard.class), card->card.getKnowledgeId() == id));
        setQuizCards(DataUtils.getAllIf(repository.getByType(QuizCard.class), card->card.getKnowledgeId() == id));
    }

    @Override
    public List<Persistent> queryLinked() {
        List<Persistent> res = new ArrayList<>();
        res.addAll(learningCards);
        res.addAll(quizCards);
        return res;
    }

    public static Knowledge protoType() {
        Knowledge res = new Knowledge();
        res.setId(SpringUtils.getBean(KTree.class).size());
        res.setSuperKnowledgeId(-1);
        res.setDescription("");
        res.setMasteryMin(0);
        res.setMasteryMax(100);
        res.setQuizCards(new ArrayList<>());
        res.setLearningCards(new ArrayList<>());
        return res;
    }

}
