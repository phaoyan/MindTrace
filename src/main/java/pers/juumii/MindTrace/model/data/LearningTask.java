package pers.juumii.MindTrace.model.data;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LearningTask implements Persistent{

    private int id;
    private int knowledgeId;
    private int superTaskId;
    private String desc;

    public boolean isLike(String keyword) {
        return desc.contains(keyword);
    }

    public void clear(){
        knowledgeId = 0;
        superTaskId = 0;
        desc = null;
    }

    public boolean isClear(){
        return knowledgeId == 0 && desc == null && superTaskId == 0;
    }
}
