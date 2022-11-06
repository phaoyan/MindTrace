package pers.juumii.MindTrace.model.service.mastery;

import lombok.Getter;
import lombok.Setter;
import pers.juumii.MindTrace.model.data.Knowledge;

public class LearningBasedStrategy implements EvaluationStrategy{
    @Setter
    @Getter
    private int finalCompletion;

    public LearningBasedStrategy(int finalCompletion) {
        this.finalCompletion = finalCompletion;
    }

    @Override
    public void evaluate(Knowledge knowledge) {}
}
