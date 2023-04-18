package hello;

import org.json.JSONArray;
import org.json.JSONObject;

public class ComparisonColumns {
    private ComparisonColumn[] comparisonColumns;

    public ComparisonColumns(ComparisonColumn[] comparisonColumns) {
        this.comparisonColumns = comparisonColumns;
    }

    public ComparisonColumns(JSONArray compareArray){

        ComparisonColumn[] constructedCompareArray = new ComparisonColumn[compareArray.length()];
        for (int j = 0; j < compareArray.length(); j++) {

            JSONObject compareObj = compareArray.getJSONObject(j);
            String leftColumnCompare = compareObj.getString("left_column");
            String rightColumnCompare = compareObj.getString("right_column");
            String compareCondition = compareObj.getString("condition");

            constructedCompareArray[j] = new ComparisonColumn(leftColumnCompare,rightColumnCompare,compareCondition);

        }

        this.comparisonColumns = constructedCompareArray;
    }
    public ComparisonColumn[] getComparisonColumns() {
        return comparisonColumns;
    }

    public void setComparisonColumns(ComparisonColumn[] comparisonColumns) {
        this.comparisonColumns = comparisonColumns;
    }
}

