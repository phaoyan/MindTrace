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

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@InstantData
public class QuizRecord implements Persistent {

    private long id, cardId;
    private double completion;
    private String description;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;


    public static QuizRecord protoType() {
        QuizRecord record = new QuizRecord();
        record.setId(MathUtils.unique());
        record.setTime(LocalDateTime.now());
        return record;
    }
}
