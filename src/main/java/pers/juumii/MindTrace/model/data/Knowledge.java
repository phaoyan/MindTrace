package pers.juumii.MindTrace.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Knowledge implements Persistent{

    private int id;
    private int superKnowledgeId;
    private String desc;
    private int masteryMin;
    private int masteryMax;

    public boolean isLike(String keyword) {
        return desc.contains(keyword);
    }

    public void clear(){
        desc = null;
        masteryMin = 0;
        masteryMax = 0;
        superKnowledgeId = 0;
    }

    public boolean isClear(){
        return desc == null && masteryMin == 0 && masteryMax == 0 && superKnowledgeId == 0;
    }


    public int getMasteryAvg() {
        return (masteryMax + masteryMin) / 2;
    }
}
