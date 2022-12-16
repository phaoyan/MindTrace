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

    public static final int NO_RECORD = -1, POOR = 0, GOOD = 1, PERFECT = 2;

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

    // -1 for no record, 0 for poor, 1 for good, 2 for perfect
    @InstantData
    public int getCompletionLevel() {
        if(quizRecords.isEmpty())
            return NO_RECORD;
        QuizRecord lastRecord = quizRecords.get(quizRecords.size() - 1);
        return  lastRecord.getCompletion() <= 30 ? POOR :
                lastRecord.getCompletion() < 90 ? GOOD :
                PERFECT;
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
