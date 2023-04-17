package hello;

import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;

public class HelloWorld {
    public static void main(String[] args) {
//        String jsonString = "[{\"left_table\":{\"database\":\"datalake\",\"name\":\"dim_campaign\",\"columns\":[\"id\",\"label\",\"client_id\",\"campaign_id\"],\"sort_columns\":[\"id\",\"label\",\"client_id\",\"campaign_id\"],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"right_table\":{\"database\":\"datalake\",\"name\":\"dim_aimconfigs_campaign\",\"columns\":[\"id\",\"label\",\"client_id\",\"campaign_id\",\"process_date\"],\"sort_columns\":[\"id\",\"label\",\"client_id\",\"campaign_id\"],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"join\":{\"method\":\"FULL\",\"on\":[[\"id\",\"id\",\"EQUAL\"]],\"sort_join\":false,\"unique_join\":false},\"compare\":[[\"label\",\"label\",\"NOT_EQUAL\"],[\"client_id\",\"client_id\",\"NOT_EQUAL\"],[\"campaign_id\",\"campaign_id\",\"NOT_EQUAL\"]]},{\"left_table\":{\"database\":\"datalake\",\"name\":\"dim_partner_deactivation\",\"columns\":[\"partner_deactivation_id\",\"record_created_ts\",\"partner_name\",\"effective_from_ts\",\"effective_to_ts\"],\"sort_columns\":[\"partner_deactivation_id\",\"record_created_ts\",\"partner_name\",\"effective_from_ts\",\"effective_to_ts\"],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"right_table\":{\"database\":\"datalake\",\"name\":\"dim_aimconfigs_partner_deactivation\",\"columns\":[\"partner_deactivation_id\",\"record_created_ts\",\"partner_name\",\"effective_from_ts\",\"effective_to_ts\",\"process_date\"],\"sort_columns\":[\"partner_deactivation_id\",\"record_created_ts\",\"partner_name\",\"effective_from_ts\",\"effective_to_ts\"],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"join\":{\"method\":\"FULL\",\"on\":[[\"partner_deactivation_id\",\"partner_deactivation_id\",\"EQUAL\"]],\"sort_join\":false,\"unique_join\":false},\"validate_unique\":true,\"compare\":[[\"record_created_ts\",\"record_created_ts\",\"NOT_EQUAL\"],[\"partner_name\",\"partner_name\",\"NOT_EQUAL\"],[\"effective_from_ts\",\"effective_from_ts\",\"NOT_EQUAL\"],[\"effective_to_ts\",\"effective_to_ts\",\"NOT_EQUAL\"]]}]";
        String jsonString = "[{\"left_table\":{\"database\":\"datalake\",\"name\":\"dim_campaign\",\"columns\":[\"id\",\"label\",\"client_id\",\"campaign_id\"],\"sort_columns\":[{\"column\":\"id\",\"order_type\":\"ASC\"},{\"column\":\"label\",\"order_type\":\"DESC\"},{\"column\":\"client_id\",\"order_type\":\"ASC\"},{\"column\":\"campaign_id\",\"order_type\":\"ASC\"}],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"right_table\":{\"database\":\"datalake\",\"name\":\"dim_aimconfigs_campaign\",\"columns\":[\"id\",\"label\",\"client_id\",\"campaign_id\",\"process_date\"],\"sort_columns\":[{\"column\":\"id\",\"order_type\":\"ASC\"},{\"column\":\"label\",\"order_type\":\"DESC\"},{\"column\":\"client_id\",\"order_type\":\"ASC\"},{\"column\":\"campaign_id\",\"order_type\":\"ASC\"}],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"join\":{\"method\":\"FULL\",\"on\":[{\"left_column\":\"id\",\"right_column\":\"id\",\"condition\":\"EQUAL\"}],\"sort_join\":false,\"unique_join\":false},\"compare\":[{\"left_column\":\"label\",\"right_column\":\"label\",\"condition\":\"NOT_EQUAL\"},{\"left_column\":\"client_id\",\"right_column\":\"client_id\",\"condition\":\"NOT_EQUAL\"},{\"left_column\":\"campaign_id\",\"right_column\":\"campaign_id\",\"condition\":\"NOT_EQUAL\"}]}]";
/*
        String jsonString = "[\n" +
                "    {\n" +
                "        \"left_table\": {\n" +
                "            \"database\":\"datalake\",\n" +
                "            \"name\": \"dim_campaign\",\n" +
                "            \"columns\": [\"id\", \"label\", \"client_id\", \"campaign_id\"],\n" +
                "            \"sort_columns\": [\"id\", \"label\", \"client_id\", \"campaign_id\"],\n" +
                "            \"generate_unique_id\": false,\n" +
                "            \"generate_sort_id\": false,\n" +
                "            \"filter\": []\n" +
                "        },\n" +
                "        \"right_table\": {\n" +
                "            \"database\":\"datalake\",\n" +
                "            \"name\": \"dim_aimconfigs_campaign\",\n" +
                "            \"columns\": [\"id\", \"label\", \"client_id\", \"campaign_id\", \"process_date\"],\n" +
                "            \"sort_columns\": [\"id\", \"label\", \"client_id\", \"campaign_id\"],\n" +
                "            \"generate_unique_id\": false,\n" +
                "            \"generate_sort_id\": false,\n" +
                "            \"filter\": []\n" +
                "        },\n" +
                "        \"join\": {\n" +
                "            \"method\":\"FULL\",\n" +
                "            \"on\": [[\"id\",\"id\", \"EQUAL\"]],\n" +
                "            \"sort_join\": false,\n" +
                "            \"unique_join\": false\n" +
                "        },\n" +
                "        \"compare\": [\n" +
                "            {\n" +
                "                \"left_column\" : \"label\",\n" +
                "                \"right_column\": \"label\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"client_id\",\n" +
                "                \"right_column\": \"client_id\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"campaign_id\",\n" +
                "                \"right_column\": \"campaign_id\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";

 */
        try {
            JSONArray jsonArray = new JSONArray(jsonString); // Parse JSON array string
//            JSONObject jsonObject = new JSONObject(); // Create new JSON object
//            jsonObject.put("data", jsonArray);

            // Loop through each object in the array
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);

                JSONObject leftTableObject = obj.getJSONObject("left_table");
                JSONArray leftTableSortColumnsArray = leftTableObject.getJSONArray("sort_columns");

                JSONObject rightTableObject = obj.getJSONObject("right_table");
                JSONArray rightTableSortColumnsArray = rightTableObject.getJSONArray("sort_columns");

                JSONObject joinObject = obj.getJSONObject("join");
                JSONArray joinOnArray = joinObject.getJSONArray("on");

                JSONArray compareArray = obj.getJSONArray("compare");


                System.out.println("leftTable DB-->" + leftTableObject.getString("database"));
                System.out.println("leftTable name-->" + leftTableObject.getString("name"));
                System.out.println("leftTable columns-->" + leftTableObject.get("columns"));

                // Loop through each leftTableSortColumnsArray
                for(int p=0; p<leftTableSortColumnsArray.length(); p++){
                    JSONObject sortObject = leftTableSortColumnsArray.getJSONObject(p);

                    String sortOrder = sortObject.getString("column");
                    String sortOrderType = sortObject.getString("order_type");

                    System.out.println("sortOrder-->" + sortOrder);
                    System.out.println("sortOrderType-->" + sortOrderType);

                }

                System.out.println("leftTable generate_unique_id-->" + leftTableObject.getBoolean("generate_unique_id"));
                System.out.println("leftTable generate_sort_id-->" + leftTableObject.getBoolean("generate_sort_id"));
                System.out.println("leftTable filter[]-->" + leftTableObject.get("filter"));

                System.out.println("Join method-->" + joinObject.getString("method"));

                for (int k = 0; k < joinOnArray.length(); k++) {
                    JSONObject joinOnObject = joinOnArray.getJSONObject(k);

                    String leftColumnJoin = joinOnObject.getString("left_column");
                    String rightColumnJoin = joinOnObject.getString("right_column");
                    String joinCondition = joinOnObject.getString("condition");

                    System.out.println("leftColumnJoin-->" + leftColumnJoin);
                    System.out.println("rightColumnJoin-->" + rightColumnJoin);
                    System.out.println("joinCondition-->" + joinCondition);

                }

                System.out.println("sort_join-->" + joinObject.getBoolean("sort_join"));
                System.out.println("unique_join-->" + joinObject.getBoolean("unique_join"));

                // Loop through each compare array in the object
                for (int j = 0; j < compareArray.length(); j++) {

                    JSONObject compareObj = compareArray.getJSONObject(j);
                    String leftColumn = compareObj.getString("left_column");
                    String rightColumn = compareObj.getString("right_column");
                    String condition = compareObj.getString("condition");

                    System.out.println("leftColumn compare -->" + leftColumn);
                    System.out.println("rightColumn compare-->" + rightColumn);
                    System.out.println("compare condition-->" + condition);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
