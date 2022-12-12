package pers.juumii.MindTrace.model.service.quiz.generator;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KNode;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Stack;

/**
 * 实现覆盖的知识点尽可能少地生成Quiz
 */
@Service
public class VerticalQuizGenerator extends QuizGenerator{

    @Override
    public List<QuizCard> quizzes(int scale, KTree kTree) {
        return QuizGenerator.quizzes(scale, kTree, repo -> {
            Stack<KNode> randomKNodes = DataUtils.randomStackOf(kTree.getRoot().getKNodesBeneath(), LocalDateTime.now().getDayOfYear());
            Stack<QuizCard> res = new Stack<>();
            while (!randomKNodes.isEmpty())
                randomKNodes.pop().getData().getQuizCards().forEach(res::push);
            return res;
        });
    }

    public static ObjectNode defaultConfigs(){
        return new ObjectNode(JsonNodeFactory.instance);
    }

}
