package pers.juumii.MindTrace.model.service.ktree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.LearningCard;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.util.ArrayList;
import java.util.List;


@Data
@Service
public class KTree {

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

    public KNode queryKNode(long id){
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

    public List<QuizCard> queryQuizCards(){
        List<QuizCard> res = new ArrayList<>();
        root.queryKNodeBeneath().forEach(kNode -> res.addAll(kNode.getData().getQuizCards()));
        return res;
    }

}
