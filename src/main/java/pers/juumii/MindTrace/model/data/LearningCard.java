package pers.juumii.MindTrace.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.ktree.Repository;
import pers.juumii.MindTrace.utils.DataUtils;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class LearningCard implements Linkable{

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime establishTime;
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
        res.setDescription("");
        res.setResource("");
        res.setEstablishTime(LocalDateTime.now());
        return res;
    }

}
