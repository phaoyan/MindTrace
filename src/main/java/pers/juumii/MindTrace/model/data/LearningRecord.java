package pers.juumii.MindTrace.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class LearningRecord implements Persistent {

    private int id, cardId, completion;
    private String description;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    public static LearningRecord protoType() {
        LearningRecord record = new LearningRecord();
        record.setId(SpringUtils.getBean(KTree.class).learningRecordSize());
        record.setTime(LocalDateTime.now());
        return record;
    }
    public boolean isLike(String keyword) {
        return description.contains(keyword);
    }


}
