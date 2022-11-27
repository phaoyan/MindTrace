package pers.juumii.MindTrace.model.service.quiz.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
@Qualifier("RandomQuizGenerator")
public class RandomQuizGenerator extends QuizGenerator{

    @Autowired
    public RandomQuizGenerator(KTree kTree) {
        super(kTree);
    }

    @Override
    public List<QuizCard> quizzes() {
        if(kTree.getRoot() == null)
            return new ArrayList<>();
        List<QuizCard> res = new ArrayList<>();

        Stack<QuizCard> quizStack = DataUtils.randomStackOf(kTree.getQuizCards());
        int restScale = scale;
        while (!quizStack.isEmpty() && restScale > 0){
            QuizCard cur = quizStack.pop();
            res.add(cur);
            restScale -= cur.getScale() == 0 ? 1 : cur.getScale();
        }
        return res;
    }
}
