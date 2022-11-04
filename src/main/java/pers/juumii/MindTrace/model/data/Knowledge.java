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

    private int id, superKnowledgeId;
    private int masteryMin, masteryMax;
    private String description;

    public boolean isLike(String keyword) {
        return description.contains(keyword);
    }

    public int getMasteryAvg() {
        return (masteryMax + masteryMin) / 2;
    }

}
