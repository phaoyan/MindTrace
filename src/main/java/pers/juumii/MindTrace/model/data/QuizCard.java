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
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@InstantData
public class QuizCard implements Persistent{

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime establishTime;
    private long id, knowledgeId;
    private int scale; //minutes
    private double rate = 2.5;  // 0 ~ 5，默认2.5
    private String description, front, back;
    private List<QuizRecord> quizRecords = new ArrayList<>();

    public LocalDateTime getEstablishTime(){
        return establishTime == null ? LocalDateTime.now() : establishTime;
    }

    public static QuizCard protoType() {
        QuizCard res = new QuizCard();
        res.setId(MathUtils.unique());
        res.setKnowledgeId(-1);
        res.setFront("");
        res.setBack("");
        res.setDescription("");
        res.setEstablishTime(LocalDateTime.now());
        res.setScale(1);
        return res;
    }

    public static QuizCard mock(double rate, int minusDays, double completion){
        QuizCard quizCard = new QuizCard();
        quizCard.setRate(rate);
        QuizRecord record = new QuizRecord();
        record.setTime(LocalDateTime.now().minusDays(minusDays));
        record.setCompletion(completion);
        return quizCard;
    }

}
