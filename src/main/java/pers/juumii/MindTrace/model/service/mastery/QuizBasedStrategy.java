package pers.juumii.MindTrace.model.service.mastery;

import lombok.Getter;
import lombok.Setter;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.QuizRecord;
import pers.juumii.MindTrace.model.service.LinkingSearcher;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.util.List;

public class QuizBasedStrategy implements EvaluationStrategy{
    @Getter
    @Setter
    private int divider;

    public QuizBasedStrategy(int divider) {
        this.divider = divider;
    }

    @Override
    public void evaluate(Knowledge knowledge) {
        LinkingSearcher searcher = SpringUtils.getBean(LinkingSearcher.class);
        List<QuizRecord> quizRecords = searcher.getQuizRecord(knowledge);
        int bottom = 0;
        int top = 100;
        for(QuizRecord quizRecord: quizRecords){
            int completion = quizRecord.getCompletion();
            bottom = (bottom * (divider-1) + completion) / divider;
            top = (top * (divider-1) + completion) / divider;
        }
        knowledge.setMasteryMin(bottom);
        knowledge.setMasteryMax(top);
    }
}
