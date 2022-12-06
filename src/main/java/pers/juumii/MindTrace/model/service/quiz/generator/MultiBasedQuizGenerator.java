package pers.juumii.MindTrace.model.service.quiz.generator;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 实现多因素加权生成Quiz，对应WeightedSort
 */
@Service
public class MultiBasedQuizGenerator extends QuizGenerator{

    @Getter
    @Setter
    private Map<String, Double> weightMap;

    @Override
    public List<QuizCard> quizzes(int scale, KTree kTree) {
        Map<Function<QuizCard, Double>, Double> weightMap = getRealWeightMap();
        return QuizGenerator.quizzes(scale, kTree, repo -> {
            DataUtils.weightedSort(repo, weightMap);
            return DataUtils.stackOf(repo);
        });
    }

    public static ObjectNode defaultConfigs(){
        ObjectNode res = new ObjectNode(JsonNodeFactory.instance);
        ObjectNode weightMap = new ObjectNode(JsonNodeFactory.instance);
        weightMap.put("rateEvaluator", 30.0);
        weightMap.put("recordCompletionEvaluator", 1.0);
        weightMap.put("recordTimeEvaluator", 6.0);
        res.set("weightMap", weightMap);
        return res;
    }

    private Map<Function<QuizCard, Double>, Double> getRealWeightMap() {
        if(weightMap == null || weightMap.isEmpty())
            return defaultWeightMap();
        Map<Function<QuizCard, Double>, Double> res = new HashMap<>();
        for(String evaluator: weightMap.keySet()){
            switch (evaluator){
                case "rateEvaluator" -> res.put(rateEvaluator(), weightMap.get(evaluator));
                case "recordCompletionEvaluator" -> res.put(recordCompletionEvaluator(), weightMap.get(evaluator));
                case "recordTimeEvaluator" -> res.put(recordTimeEvaluator(), weightMap.get(evaluator));
            }
        }
        return res;
    }

    public static Map<Function<QuizCard, Double>, Double> defaultWeightMap(){
        Map<Function<QuizCard, Double>, Double> res = new HashMap<>();
        res.put(rateEvaluator(), 30.0);
        res.put(recordCompletionEvaluator(), 1.0);
        res.put(recordTimeEvaluator(), 6.0);
        return res;
    }

    public static Function<QuizCard, Double> recordTimeEvaluator(){
        return card-> (double) Duration.between(card.getQuizRecords().isEmpty() ? card.getEstablishTime(): DataUtils.getLast(card.getQuizRecords()).getTime(), LocalDateTime.now()).toDays();
    }
    public static Function<QuizCard, Double> recordCompletionEvaluator(){
        //带负号的原因：完成度越高，其优先级越低
        return card -> -(card.getQuizRecords().isEmpty() ? 50: DataUtils.getLast(card.getQuizRecords()).getCompletion());
    }
    public static Function<QuizCard, Double> rateEvaluator(){
        return QuizCard::getRate;
    }

}
