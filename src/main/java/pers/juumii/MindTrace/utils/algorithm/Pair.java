package pers.juumii.MindTrace.utils.algorithm;

import lombok.Data;

@Data
public class Pair <A,B>{
    private A left;
    private B right;

    public Pair(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public Pair() {}
}
