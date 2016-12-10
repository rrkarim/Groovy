package Classes;

import java.io.Serializable;

/**
 * Created by YoAtom on 12/10/2016.
 */

public class Pair implements Serializable {
    private Integer F, S;
    Pair() {}
    Pair(int F, int S) {
        this.F = F;
        this.S = S;
    }
    int first() {
        return this.F;
    }
    int second() {
        return this.S;
    }
    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + (F == null ? 0 : F.hashCode());
        hash = hash * 31 + (S == null ? 0 : S.hashCode());
        return hash;
    }
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Pair)) return false;
        Pair p = (Pair) other;
        return (this.F == p.F) && (this.S == p.S);
    }
}
