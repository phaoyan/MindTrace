package pers.juumii.MindTrace.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class Knowledge implements Persistent, Linkable{

    private int id, superKnowledgeId;
    private int masteryMin, masteryMax;
    private String description;
    private List<LearningCard> learningCards;
    private List<QuizCard> quizCards;

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

}
