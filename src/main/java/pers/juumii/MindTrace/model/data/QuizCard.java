package pers.juumii.MindTrace.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class QuizCard implements Persistent, Linkable{

    private int id, knowledgeId;
    private String description, front, back;
    private List<QuizRecord> quizRecords;

    public boolean isLike(String keyword) {
        return description.contains(keyword);
    }

    //组装：连接属于这个quizCard的quizRecord
    @Override
    public void link(Repository repository){
        setQuizRecords(DataUtils.getAllIf(repository.getByType(QuizRecord.class), record->record.getCardId()==id));
    }
}
