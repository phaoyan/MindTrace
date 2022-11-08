package pers.juumii.MindTrace.model.data;

import lombok.*;
import pers.juumii.MindTrace.model.service.KTree;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.utils.DataUtils;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class LearningCard implements Linkable{

    private int id, knowledgeId;
    private String description, resource;
    private List<LearningRecord> learningRecords;

    public boolean isLike(String keyword) {
        return description.contains(keyword);
    }

    //组装：连接属于这个learningCard的learningRecord
    @Override
    public void link(Repository repository){
        setLearningRecords(DataUtils.getAllIf(repository.getByType(LearningRecord.class), record->record.getCardId()==id));
    }

    @Override
    public List<Persistent> queryLinked() {
        return new ArrayList<>(learningRecords);
    }

    public static LearningCard protoType() {
        LearningCard res = new LearningCard();
        res.setId(SpringUtils.getBean(KTree.class).learningCardSize()+1);
        res.setKnowledgeId(-1);
        res.setLearningRecords(new ArrayList<>());
        res.setDescription("empty...");
        res.setResource("empty...");
        return res;
    }
}
