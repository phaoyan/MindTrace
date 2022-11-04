package pers.juumii.MindTrace.model.data;

import lombok.*;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class LearningCard implements Persistent, Linkable{

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
}
