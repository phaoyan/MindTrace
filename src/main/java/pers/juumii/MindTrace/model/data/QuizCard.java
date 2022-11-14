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
public class QuizCard implements Linkable{

    private int id, knowledgeId;
    private String description, front, back;
    private List<QuizRecord> quizRecords = new ArrayList<>();

    public static QuizCard protoType() {
        QuizCard res = new QuizCard();
        res.setId(SpringUtils.getBean(KTree.class).quizCardSize()+1);
        res.setKnowledgeId(-1);
        res.setFront("");
        res.setBack("");
        res.setDescription("");
        return res;
    }

    public boolean isLike(String keyword) {
        return description.contains(keyword);
    }

    //组装：连接属于这个quizCard的quizRecord
    @Override
    public void link(Repository repository){
        setQuizRecords(DataUtils.getAllIf(repository.getByType(QuizRecord.class), record->record.getCardId()==id));
    }

    @Override
    public List<Persistent> queryLinked() {
        return new ArrayList<>(quizRecords);
    }
}
