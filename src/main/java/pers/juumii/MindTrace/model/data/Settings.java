package pers.juumii.MindTrace.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Settings implements Persistent{
    private int id;
    private String k, v;

    @Override
    public int getId() {
        return id;
    }
    @Override
    public boolean isLike(String keyword) {
        return k.contains(keyword);
    }
}
