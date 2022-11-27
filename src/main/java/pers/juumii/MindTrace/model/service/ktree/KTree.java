package pers.juumii.MindTrace.model.service.ktree;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.LearningCard;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class KTree {

    @Getter
    @Setter
    private KTreeLoader loader;
    @Getter
    @Setter
    private KNode root;

    @Autowired
    public KTree(@Qualifier("KTreeJsonLoader") KTreeLoader loader) {
        this.loader = loader;
        load();
    }

    public void load() {
        loader.load(this);
    }

    //将KTree中储存的数据同步到repository
    public void synchronize(){
        loader.synchronize(this);
    }

    public KNode getKNode(int id){
        return id == 0 ? root: DataUtils.getIf(root.queryKNodeBeneath(), kNode->kNode.getData().getId() == id);
    }

    public int size() {
        return root.size();
    }

    public int quizCardSize(){
        int res = 0;
        for(KNode kNode: root.queryKNodeBeneath())
            res += kNode.getData().getQuizCards().size();
        return res;
    }

    public int learningCardSize(){
        int res = 0;
        for(KNode kNode: root.queryKNodeBeneath())
            res += kNode.getData().getLearningCards().size();
        return res;
    }

    public int quizRecordSize(){
        int res = 0;
        for(KNode kNode: root.queryKNodeBeneath())
            for(QuizCard card: kNode.getData().getQuizCards())
                res += card.getQuizRecords().size();
        return res;
    }

    public int learningRecordSize(){
        int res = 0;
        for(KNode kNode: root.queryKNodeBeneath())
            for(LearningCard card: kNode.getData().getLearningCards())
                res += card.getLearningRecords().size();
        return res;
    }

    public List<QuizCard> getQuizCards(){
        List<QuizCard> res = new ArrayList<>();
        root.queryKNodeBeneath().forEach(kNode -> res.addAll(kNode.getData().getQuizCards()));
        return res;
    }

}
