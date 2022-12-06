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
public class Knowledge implements Persistent{

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime establishTime;
    private long id, superKnowledgeId;
    private double rate = 2.5; // 0 ~ 5
    private String description;
    private List<LearningCard> learningCards = new ArrayList<>();
    private List<QuizCard> quizCards = new ArrayList<>();



    public static Knowledge protoType() {
        Knowledge res = new Knowledge();
        res.setId(MathUtils.unique());
        res.setSuperKnowledgeId(-1);
        res.setDescription("");
        res.setQuizCards(new ArrayList<>());
        res.setLearningCards(new ArrayList<>());
        res.setEstablishTime(LocalDateTime.now());
        return res;
    }

}
