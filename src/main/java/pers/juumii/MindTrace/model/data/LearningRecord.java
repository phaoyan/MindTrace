package pers.juumii.MindTrace.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pers.juumii.MindTrace.utils.algorithm.MathUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@InstantData
public class LearningRecord implements Persistent {

    private long id;
    private String description;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime, finishTime;
    private List<Long> concernedKnowledgeIds = new ArrayList<>();

    @InstantData
    public Duration getDuration(){
        return finishTime == null ? Duration.ZERO : Duration.between(startTime, finishTime);
    }

    public static LearningRecord protoType() {
        LearningRecord record = new LearningRecord();
        record.setId(MathUtils.unique());
        record.setStartTime(LocalDateTime.now());
        record.setConcernedKnowledgeIds(new ArrayList<>());
        return record;
    }
}
