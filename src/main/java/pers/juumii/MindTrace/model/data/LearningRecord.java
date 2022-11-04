package pers.juumii.MindTrace.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LearningRecord implements Persistent {

    private int id, cardId, completion;
    private String description;
    private LocalDateTime time;

    public boolean isLike(String keyword) {
        return description.contains(keyword);
    }


}
