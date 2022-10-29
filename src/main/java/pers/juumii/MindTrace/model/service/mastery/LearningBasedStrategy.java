package pers.juumii.MindTrace.model.service.mastery;

import lombok.Getter;
import lombok.Setter;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.LearningRecord;
import pers.juumii.MindTrace.model.service.LinkingSearcher;
import pers.juumii.MindTrace.utils.SpringUtils;

public class LearningBasedStrategy implements EvaluationStrategy{
    @Setter
    @Getter
    private int finalCompletion;

    public LearningBasedStrategy(int finalCompletion) {
        this.finalCompletion = finalCompletion;
    }

    @Override
    public void evaluate(Knowledge knowledge) {
        LinkingSearcher searcher = SpringUtils.getBean(LinkingSearcher.class);
        int totalCompletion = 0;
        for(LearningRecord record: searcher.getLearningRecord(knowledge))
            totalCompletion += record.getCompletion();
        knowledge.setMasteryMin((int)((double)totalCompletion / finalCompletion * 100));
        knowledge.setMasteryMax((int)((double)totalCompletion / finalCompletion * 100));
    }
}
