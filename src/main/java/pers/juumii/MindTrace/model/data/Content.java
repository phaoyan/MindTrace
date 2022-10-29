package pers.juumii.MindTrace.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Content implements Persistent{

    private int id;
    private int taskId;
    private String src;
    private String desc;

    public boolean isLike(String keyword) {
        return desc.toLowerCase().contains(keyword.toLowerCase());
    }

    public void clear(){
        taskId = 0;
        src = null;
        desc = null;
    }

    public boolean isClear(){
        return taskId == 0 && src == null && desc == null;
    }

}
