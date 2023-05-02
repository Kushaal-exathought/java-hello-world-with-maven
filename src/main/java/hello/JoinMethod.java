package hello;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;

enum JoinType {
    FULL,
    INNER,
    LEFT,
    RIGHT,
    CROSS
}

public class JoinMethod {

    private JoinType method;
    private ComparisonColumn[] on;
    private boolean sortJoin;
    private boolean uniqueJoin;

    // Constructors
    public JoinMethod(JSONObject joinObject) {
        this.method = JoinType.valueOf(joinObject.getString("method").toUpperCase());

        JSONArray joinOnArray = joinObject.getJSONArray("on");
        ComparisonColumn[] joinOn = new ComparisonColumn[joinOnArray.length()];
        for (int k = 0; k < joinOnArray.length(); k++) {
            JSONObject joinOnObject = joinOnArray.getJSONObject(k);
            String leftColumnJoin = joinOnObject.getString("left_column");
            String rightColumnJoin = joinOnObject.getString("right_column");
            String joinCondition = joinOnObject.getString("condition");

            joinOn[k] = new ComparisonColumn(leftColumnJoin, rightColumnJoin, joinCondition);
        }
        this.on = joinOn;

        this.sortJoin = joinObject.getBoolean("sort_join");
        this.uniqueJoin = joinObject.getBoolean("unique_join");
    }

    // Getters and setters

    public JoinType getMethod() {
        return method;
    }

    public void setMethod(JoinType method) {
        this.method = method;
    }

    public ComparisonColumn[] getOn() {
        return on;
    }

    public void setOn(ComparisonColumn[] on) {
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
                "method=" + method +
                ", on=" + Arrays.toString(on) +
                ", sortJoin=" + sortJoin +
                ", uniqueJoin=" + uniqueJoin +
                '}';
    }
}
