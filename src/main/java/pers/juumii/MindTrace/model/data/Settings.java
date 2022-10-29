package pers.juumii.MindTrace.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Settings implements Persistent{
    private int id;
    private String k, v;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void clear() {
        k = null;
        v = null;
    }

    @Override
    public boolean isClear() {
        return k == null && v == null;
    }

    @Override
    public boolean isLike(String keyword) {
        return k.contains(keyword);
    }
}
