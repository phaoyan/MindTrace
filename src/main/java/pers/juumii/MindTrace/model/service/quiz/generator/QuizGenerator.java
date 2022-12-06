package pers.juumii.MindTrace.model.service.quiz.generator;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.statistics.Statistics;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.JsonUtils;
import pers.juumii.MindTrace.utils.SpringUtils;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.util.*;
import java.util.function.Function;

@Service
public abstract class QuizGenerator {

    private static final String packagePath =  "pers.juumii.MindTrace.model.service.quiz.generator.";

    public abstract List<QuizCard> quizzes(int scale, KTree kTree);

    public static QuizGenerator load(List<QuizGenerator> quizGenerators, ObjectNode data) throws ClassNotFoundException {
        String type = data.get("type").asText();
        ObjectNode configs = data.with("configs");
        QuizGenerator res = null;
        for(QuizGenerator quizGenerator: quizGenerators)
            if(Class.forName(packagePath + type).isInstance(quizGenerator))
                res = JsonUtils.readJson(JsonUtils.toJson(configs), quizGenerator.getClass());
        return res;
    }

    public static List<QuizCard> quizzes(int scale, KTree kTree, Function<List<QuizCard>, Stack<QuizCard>> strategy){
        if(kTree.getRoot() == null)
            return new ArrayList<>();

        Statistics statistics = SpringUtils.getBean(Statistics.class);
        List<QuizCard> res = new ArrayList<>();

        //总的规模减去当天已经完成了的部分。如果减得负数或0，则下面的循环自然不会进行
        scale -= statistics.completedQuizCardScale();

        //将当天做过的卡片排除在外
        List<QuizCard> quizCards = kTree.queryQuizCards();
        DataUtils.difference(quizCards, statistics.completedQuizCards());

        //应用排序策略，每确定一个考察的卡片就缩减总的quizScale
        Stack<QuizCard> quizStack = strategy.apply(quizCards);
        int restScale = scale;
        while (!quizStack.isEmpty() && restScale > 0){
            QuizCard cur = quizStack.pop();
            res.add(cur);
            restScale -= cur.getScale() == 0 ? 1 : cur.getScale();
        }
        return res;
    }

    public static ObjectNode defaultConfigs(String type){
        String className = packagePath + type;
        if(className.equals(CompositionalQuizGenerator.class.getName()))
            return CompositionalQuizGenerator.defaultConfigs();
        else if(className.equals(HorizontalQuizGenerator.class.getName()))
            return HorizontalQuizGenerator.defaultConfigs();
        else if(className.equals(MultiBasedQuizGenerator.class.getName()))
            return MultiBasedQuizGenerator.defaultConfigs();
        else if(className.equals(RandomQuizGenerator.class.getName()))
            return RandomQuizGenerator.defaultConfigs();
        else if(className.equals(VerticalQuizGenerator.class.getName()))
            return VerticalQuizGenerator.defaultConfigs();
        else return null;
    }

}
