package pers.juumii.MindTrace.model.service.mastery;

import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.Level;

public interface EvaluationStrategy {

    // 根据传入的知识分析掌握程度
    void evaluate(Knowledge knowledge);
}
