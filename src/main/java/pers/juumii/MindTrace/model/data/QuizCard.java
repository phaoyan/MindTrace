package pers.juumii.MindTrace.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pers.juumii.MindTrace.exception.DataClearedException;
import pers.juumii.MindTrace.model.service.Repository;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizCard implements Persistent{
    private int id, quizId;
    private String desc;
    private String front, back;

    public QuizTask getQuizTask(Repository repo){
        try {
            return repo.getById(quizId, QuizTask.class);
        } catch (DataClearedException e) {
            //如果该quizTask已经被clear，则返回空即可
            return null;
        }
    }


    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void clear() {
        quizId = 0;
        front = null;
        back = null;
    }

    @Override
    public boolean isClear() {
        return quizId == 0 && front == null && back == null;
    }

    @Override
    public boolean isLike(String keyword) {
        return false;
    }

}
