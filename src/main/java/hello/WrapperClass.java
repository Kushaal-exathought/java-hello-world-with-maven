package hello;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WrapperClass {
//    ArrayList<String> myArrayList = new ArrayList<>();
//     System.out.println("global map SIZE() ----->>>>" +((ArrayList<String>) globalMap.get("QUERY_ARRAY")).size());
    public static GenerateQuery encapsulate(JSONObject obj) {
        JSONObject leftTableObject = obj.getJSONObject("left_table");
        Table constructedLeftTableObject = new Table(leftTableObject,true);

        JSONObject rightTableObject = obj.getJSONObject("right_table");
        Table constructedRightTableObject = new Table(rightTableObject, false);

        JSONObject joinObject = obj.getJSONObject("join");
        JoinMethod constructedJoinObject = new JoinMethod(joinObject);

        JSONArray compareArray = obj.getJSONArray("compare");
        ComparisonColumns constructedCompareObject = new ComparisonColumns(compareArray);

        GenerateQuery obj1 = new GenerateQuery(constructedLeftTableObject,constructedRightTableObject,constructedJoinObject,constructedCompareObject);
        return obj1;
    }
}
