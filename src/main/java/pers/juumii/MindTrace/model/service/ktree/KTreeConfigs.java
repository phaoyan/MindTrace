package pers.juumii.MindTrace.model.service.ktree;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import pers.juumii.MindTrace.model.data.InstantData;
import pers.juumii.MindTrace.model.data.LearningCard;
import pers.juumii.MindTrace.model.data.LearningRecord;
import pers.juumii.MindTrace.utils.Constants;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@InstantData
public class KTreeConfigs {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timeAnchor;
    private int quizSchedule, quizScale;
    // type: 对应QuizGenerator的类名； Configs: 对应QuizGenerator的参数
    private ObjectNode quizGenerator;
    private List<LearningCard> learningRepository;
    private List<LearningRecord> learningRecords;

    public static KTreeConfigs getDefault(){
        KTreeConfigs res = new KTreeConfigs();
        res.setTimeAnchor(Constants.timeAnchor);
        res.setQuizSchedule(3);
        res.setQuizScale(10);
        ObjectNode quizGenerator = new ObjectNode(JsonNodeFactory.instance);
        quizGenerator.put("type", "RandomQuizGenerator");
        quizGenerator.set("configs", new ObjectNode(JsonNodeFactory.instance));
        res.setQuizGenerator(quizGenerator);
        res.setLearningRepository(new ArrayList<>());
        res.setLearningRecords(new ArrayList<>());
        return res;
    }

}
