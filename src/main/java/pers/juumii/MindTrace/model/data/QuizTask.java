package pers.juumii.MindTrace.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pers.juumii.MindTrace.model.service.Repository;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizTask implements Persistent{

    private int id;
    private int knowledgeId;
    private String desc;

    public Knowledge getKnowledge(Repository repo){
        return repo.getById(knowledgeId, Knowledge.class);
    }

    public boolean isLike(String keyword) {
        return desc.contains(keyword);
    }

    public void clear(){
        knowledgeId = 0;
        desc = null;
    }

    public boolean isClear(){
        return knowledgeId == 0 && desc == null;
    }
}
