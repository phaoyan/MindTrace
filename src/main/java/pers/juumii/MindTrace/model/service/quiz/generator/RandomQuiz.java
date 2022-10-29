package pers.juumii.MindTrace.model.service.quiz.generator;

import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.data.QuizTask;

import java.util.List;

public interface RandomQuiz {

    // 分析所有Knowledge的状态并据此判定quiz的优先级，返回一个指定数量的quizTask列表（按重要性有序排列）
    List<QuizCard> analyze(int count);
}
