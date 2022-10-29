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
public class QuizRecord implements Persistent {

    private int id;
    private int taskId;
    private String desc;
    private int completion;
    private LocalDateTime time;

    public boolean isLike(String keyword) {
        return desc.contains(keyword);
    }

    public void clear(){
        taskId = 0;
        desc = null;
        completion = 0;
        time = null;
    }

    public boolean isClear(){
        return taskId == 0 && desc == null && completion == 0 && time == null;
    }
}
