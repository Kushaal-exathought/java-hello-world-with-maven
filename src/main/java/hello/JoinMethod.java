package hello;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public JoinMethod(JSONObject joinObject) {

        this.method = joinObject.getString("method");

        JSONArray joinOnArray = joinObject.getJSONArray("on");
        ComparisonColumn[] joinOn = new ComparisonColumn[joinOnArray.length()];
        for (int k = 0; k < joinOnArray.length(); k++) {
            JSONObject joinOnObject = joinOnArray.getJSONObject(k);
            String leftColumnJoin = joinOnObject.getString("left_column");
            String rightColumnJoin = joinOnObject.getString("right_column");
            String joinCondition = joinOnObject.getString("condition");

            joinOn[k] = new ComparisonColumn(leftColumnJoin,rightColumnJoin,joinCondition);
        }
        this.on = joinOn;

        this.sortJoin = joinObject.getBoolean("sort_join");
        this.uniqueJoin = joinObject.getBoolean("unique_join");
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
