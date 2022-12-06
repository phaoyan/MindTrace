package pers.juumii.MindTrace.model.service.quiz.generator;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Qualifier("RandomQuizGenerator")
public class RandomQuizGenerator extends QuizGenerator{

    @Override
    public List<QuizCard> quizzes(int scale, KTree kTree) {
        return QuizGenerator.quizzes(scale, kTree, quizCards -> DataUtils.randomStackOf(quizCards, LocalDateTime.now().getDayOfYear()));
    }
    public static ObjectNode defaultConfigs(){
        return new ObjectNode(JsonNodeFactory.instance);
    }
}
