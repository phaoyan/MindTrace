package pers.juumii.MindTrace.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizCard implements Persistent{

    private int id, knowledgeId;
    private String description, front, back;

    public boolean isLike(String keyword) {
        return description.contains(keyword);
    }

}
