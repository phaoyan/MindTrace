package pers.juumii.MindTrace.model.service.mastery;

import lombok.Getter;
import lombok.Setter;
import pers.juumii.MindTrace.model.data.Knowledge;


public class QuizBasedStrategy implements EvaluationStrategy{
    @Getter
    @Setter
    private int divider;

    public QuizBasedStrategy(int divider) {
        this.divider = divider;
    }

    @Override
    public void evaluate(Knowledge knowledge) {

    }
}
