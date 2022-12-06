package pers.juumii.MindTrace.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import pers.juumii.MindTrace.utils.algorithm.MathUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class LearningCard implements Persistent{

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime establishTime;
    private long id, knowledgeId;
    int scale; //minutes
    private String description, resource;
    private List<LearningRecord> learningRecords;

    public static LearningCard protoType() {
        LearningCard res = new LearningCard();
        res.setId(MathUtils.unique());
        res.setKnowledgeId(-1);
        res.setLearningRecords(new ArrayList<>());
        res.setDescription("");
        res.setResource("");
        res.setEstablishTime(LocalDateTime.now());
        return res;
    }

}
