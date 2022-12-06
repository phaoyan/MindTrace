package pers.juumii.MindTrace.model.service.quiz.generator;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.util.*;

/**
 * 实现多因素按绝对优先级排列生成Quiz，对应LayeredSort
 */
@Service
public class CompositionalQuizGenerator extends QuizGenerator{

    @Getter
    @Setter
    private List<String> priority;

    @SuppressWarnings("unchecked")
    @Override
    public List<QuizCard> quizzes(int scale, KTree kTree) {
        List<Comparator<QuizCard>> priority = getRealPriority();
        return QuizGenerator.quizzes(scale, kTree, repo -> {
            DataUtils.layeredSort(repo, priority.toArray(new Comparator[0]));
            return DataUtils.stackOf(repo);
        });
    }

    private List<Comparator<QuizCard>> getRealPriority() {
        if(priority == null || priority.isEmpty())
            return defaultPriority();
        List<Comparator<QuizCard>> res = new ArrayList<>();
        for(String comparator: priority)
            switch (comparator){
                case "recordCompletionComparator" -> res.add(recordCompletionComparator());
                case "recordTimeComparator" -> res.add(recordTimeComparator());
                case "rateComparator" -> res.add(rateComparator());
            }
        return res;
    }

    private List<Comparator<QuizCard>> defaultPriority() {
        List<Comparator<QuizCard>> res = new ArrayList<>();
        res.add(recordCompletionComparator());
        res.add(rateComparator());
        res.add(recordTimeComparator());
        return res;
    }

    public static ObjectNode defaultConfigs(){
        ObjectNode res = new ObjectNode(JsonNodeFactory.instance);
        ArrayNode priority = new ArrayNode(JsonNodeFactory.instance);
        priority.add("recordCompletionComparator");
        priority.add("rateComparator");
        priority.add("recordTimeComparator");
        res.set("priority", priority);
        return res;
    }

    public static Comparator<QuizCard> recordTimeComparator(){
        return Comparator.comparing(card->
                DataUtils.getLast(card.getQuizRecords()) == null ? card.getEstablishTime() :
                        Objects.requireNonNull(DataUtils.getLast(card.getQuizRecords())).getTime());
    }
    public static Comparator<QuizCard> recordCompletionComparator(){
        //带负号的原因：完成度越高，其优先级越低
        return Comparator.comparing(card-> -(DataUtils.getLast(card.getQuizRecords()) == null ? 0: DataUtils.getLast(card.getQuizRecords()).getCompletion()));
    }
    public static Comparator<QuizCard> rateComparator(){
        return Comparator.comparing(QuizCard::getRate);
    }
}
