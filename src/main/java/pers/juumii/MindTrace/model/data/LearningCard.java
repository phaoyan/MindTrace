package pers.juumii.MindTrace.model.data;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LearningCard implements Persistent{

    private int id, knowledgeId;
    private String description, resource;

    public boolean isLike(String keyword) {
        return description.contains(keyword);
    }

}
