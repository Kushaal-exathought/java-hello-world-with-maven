package hello;

import java.util.Arrays;
import java.util.List;

public class JoinMethod {
    private String method;

    private ComparisonColumn[] on;

    private boolean sortJoin;
    private boolean uniqueJoin;

    public JoinMethod() {
    }

    public JoinMethod(String method, ComparisonColumn[] on, boolean sortJoin, boolean uniqueJoin) {
        this.method = method;
        this.on = on;
        this.sortJoin = sortJoin;
        this.uniqueJoin = uniqueJoin;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ComparisonColumn[] getJoinOn() {
        return on;
    }

    public void setJoinOn(ComparisonColumn[] on) {
        this.on = on;
    }

    public boolean isSortJoin() {
        return sortJoin;
    }

    public void setSortJoin(boolean sortJoin) {
        this.sortJoin = sortJoin;
    }

    public boolean isUniqueJoin() {
        return uniqueJoin;
    }

    public void setUniqueJoin(boolean uniqueJoin) {
        this.uniqueJoin = uniqueJoin;
    }

    @Override
    public String toString() {
        return "JoinMethod{" +
                "method='" + method + '\'' +
                ", on=" + Arrays.toString(on) +
                ", sortJoin=" + sortJoin +
                ", uniqueJoin=" + uniqueJoin +
                '}';
    }
}
