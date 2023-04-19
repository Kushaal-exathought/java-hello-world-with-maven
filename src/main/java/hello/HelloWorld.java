package hello;

import org.json.JSONArray;
import org.json.JSONObject;

public class HelloWorld {
    public static void main(String[] args) {

//        String jsonString = "[{\"left_table\":{\"database\":\"datalake\",\"name\":\"dim_campaign\",\"columns\":[\"id\",\"label\",\"client_id\",\"campaign_id\"],\"sort_columns\":[{\"column\":\"id\",\"order_type\":\"ASC\"},{\"column\":\"label\",\"order_type\":\"DESC\"},{\"column\":\"client_id\",\"order_type\":\"ASC\"},{\"column\":\"campaign_id\",\"order_type\":\"ASC\"}],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"right_table\":{\"database\":\"datalake\",\"name\":\"dim_aimconfigs_campaign\",\"columns\":[\"id\",\"label\",\"client_id\",\"campaign_id\",\"process_date\"],\"sort_columns\":[{\"column\":\"id\",\"order_type\":\"ASC\"},{\"column\":\"label\",\"order_type\":\"DESC\"},{\"column\":\"client_id\",\"order_type\":\"ASC\"},{\"column\":\"campaign_id\",\"order_type\":\"ASC\"}],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"join\":{\"method\":\"FULL\",\"on\":[{\"left_column\":\"id\",\"right_column\":\"id\",\"condition\":\"EQUAL\"}],\"sort_join\":false,\"unique_join\":false},\"compare\":[{\"left_column\":\"label\",\"right_column\":\"label\",\"condition\":\"NOT_EQUAL\"},{\"left_column\":\"client_id\",\"right_column\":\"client_id\",\"condition\":\"NOT_EQUAL\"},{\"left_column\":\"campaign_id\",\"right_column\":\"campaign_id\",\"condition\":\"NOT_EQUAL\"}]}]";
//        String jsonString = "[{\"left_table\":{\"database\":\"datalake\",\"name\":\"dim_aim_hostblocklist\",\"columns\":[\"aim_host_blocklist_id\",\"host\",\"last_updated_ts\"],\"sort_columns\":[{\"column\":\"aim_host_blocklist_id\",\"order_type\":\"ASC\"},{\"column\":\"host\",\"order_type\":\"DESC\"},{\"column\":\"last_updated_ts\",\"order_type\":\"ASC\"}],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"right_table\":{\"database\":\"datalake\",\"name\":\"dim_aimconfigs_aim_hostblocklist\",\"columns\":[\"aim_host_blocklist_id\",\"host\",\"last_updated_ts\",\"last_modified_by\",\"process_date\"],\"sort_columns\":[{\"column\":\"aim_host_blocklist_id\",\"order_type\":\"ASC\"},{\"column\":\"host\",\"order_type\":\"DESC\"},{\"column\":\"last_updated_ts\",\"order_type\":\"ASC\"},{\"column\":\"last_modified_by\",\"order_type\":\"ASC\"},{\"column\":\"process_date\",\"order_type\":\"ASC\"}],\"generate_unique_id\":false,\"generate_sort_id\":false,\"filter\":[]},\"join\":{\"method\":\"FULL\",\"on\":[{\"left_column\":\"aim_host_blocklist_id\",\"right_column\":\"aim_host_blocklist_id\",\"condition\":\"EQUAL\"}],\"sort_join\":false,\"unique_join\":false},\"compare\":[{\"left_column\":\"host\",\"right_column\":\"host\",\"condition\":\"NOT_EQUAL\"},{\"left_column\":\"last_updated_ts\",\"right_column\":\"last_updated_ts\",\"condition\":\"NOT_EQUAL\"}]}]";

        String jsonString = "[{\n" +
                "        \"left_table\": {\n" +
                "            \"database\":\"datalake\",\n" +
                "            \"name\": \"dim_aim_views\",\n" +
                "            \"columns\": [\"id\", \"organization_id\", \"domain\",\"path\",\"api_key\",\"is_default\",\"has_signal_access\",\"has_signal_npi_number\",\"has_signal_external_id\",\"has_signal_specialty\",\"has_signal_prof_desig\",\"has_signal_me_number\",\"has_signal_email\",\"is_contracted\",\"version\",\"last_updated_ts\"],\n" +
                "            \"sort_columns\": [\n" +
                "                {\n" +
                "                    \"column\": \"organization_id\",\n" +
                "                    \"order_type\": \"ASC\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"column\": \"domain\",\n" +
                "                    \"order_type\": \"DESC\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"column\": \"path\",\n" +
                "                    \"order_type\": \"ASC\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"column\": \"api_key\",\n" +
                "                    \"order_type\": \"ASC\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"column\": \"is_default\",\n" +
                "                    \"order_type\": \"ASC\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"generate_unique_id\": false,\n" +
                "            \"generate_sort_id\": false,\n" +
                "            \"filter\": []\n" +
                "        },\n" +
                "        \"right_table\": {\n" +
                "            \"database\":\"datalake\",\n" +
                "            \"name\": \"dim_aimconfigs_aim_views\",\n" +
                "            \"columns\": [\"id\", \"organization_id\", \"domain\",\"path\",\"api_key\",\"is_default\",\"has_signal_access\",\"has_signal_npi_number\",\"has_signal_external_id\",\"has_signal_specialty\",\"has_signal_prof_desig\",\"has_signal_me_number\",\"has_signal_email\",\"is_contracted\",\"version\",\"last_updated_ts\",\"last_modified_by\",\"process_date\"],\n" +
                "            \"sort_columns\": [\n" +
                "                {\n" +
                "                    \"column\": \"organization_id\",\n" +
                "                    \"order_type\": \"ASC\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"column\": \"domain\",\n" +
                "                    \"order_type\": \"DESC\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"column\": \"path\",\n" +
                "                    \"order_type\": \"ASC\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"column\": \"api_key\",\n" +
                "                    \"order_type\": \"ASC\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"column\": \"is_default\",\n" +
                "                    \"order_type\": \"ASC\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"generate_unique_id\": false,\n" +
                "            \"generate_sort_id\": false,\n" +
                "            \"filter\": []\n" +
                "        },\n" +
                "        \"join\": {\n" +
                "            \"method\":\"FULL\",\n" +
                "            \"on\": [\n" +
                "                {\n" +
                "                \"left_column\" : \"id\",\n" +
                "                \"right_column\": \"id\",\n" +
                "                \"condition\": \"EQUAL\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"sort_join\": false,\n" +
                "            \"unique_join\": false\n" +
                "        },\n" +
                "        \"compare\": [\n" +
                "            {\n" +
                "                \"left_column\" : \"organization_id\",\n" +
                "                \"right_column\": \"organization_id\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"domain\",\n" +
                "                \"right_column\": \"domain\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"path\",\n" +
                "                \"right_column\": \"path\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"api_key\",\n" +
                "                \"right_column\": \"api_key\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"is_default\",\n" +
                "                \"right_column\": \"is_default\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"has_signal_access\",\n" +
                "                \"right_column\": \"has_signal_access\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"has_signal_npi_number\",\n" +
                "                \"right_column\": \"has_signal_npi_number\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"has_signal_external_id\",\n" +
                "                \"right_column\": \"has_signal_external_id\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"has_signal_specialty\",\n" +
                "                \"right_column\": \"has_signal_specialty\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"has_signal_prof_desig\",\n" +
                "                \"right_column\": \"has_signal_prof_desig\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"has_signal_me_number\",\n" +
                "                \"right_column\": \"has_signal_me_number\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"has_signal_email\",\n" +
                "                \"right_column\": \"has_signal_email\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"is_contracted\",\n" +
                "                \"right_column\": \"is_contracted\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"version\",\n" +
                "                \"right_column\": \"version\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"left_column\" : \"last_updated_ts\",\n" +
                "                \"right_column\": \"last_updated_ts\",\n" +
                "                \"condition\": \"NOT_EQUAL\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";

        try {
            JSONArray jsonArray = new JSONArray(jsonString); // Parse JSON array string
//            JSONObject jsonObject = new JSONObject(); // Create new JSON object
//            jsonObject.put("data", jsonArray);

            // Loop through each object in the array
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
//                GenerateQuery wc =  WrapperClass.encapsulate(obj);
                GenerateQuery wc = new GenerateQuery(obj);
                wc.getOutputQuery();

//                JSONObject leftTableObject = obj.getJSONObject("left_table");
//                Table constructedLeftTableObject = new Table(leftTableObject,true);

//                JSONArray leftTableSortColumnsArray = leftTableObject.getJSONArray("sort_columns");

//                JSONObject rightTableObject = obj.getJSONObject("right_table");
//                Table constructedRightTableObject = new Table(rightTableObject, false);

//                JSONArray rightTableSortColumnsArray = rightTableObject.getJSONArray("sort_columns");

//                JSONObject joinObject = obj.getJSONObject("join");
//                JoinMethod constructedJoinObject = new JoinMethod(joinObject);
//                JSONArray joinOnArray = joinObject.getJSONArray("on");

//                JSONArray compareArray = obj.getJSONArray("compare");
//                ComparisonColumns constructedCompareObject = new ComparisonColumns(compareArray);
//
//
//                GenerateQuery obj1 = new GenerateQuery(constructedLeftTableObject,constructedRightTableObject,constructedJoinObject,constructedCompareObject);
//                obj1.getOutputQuery();

//                System.out.println("leftTable DB-->" + leftTableObject.getString("database"));
//                System.out.println("leftTable name-->" + leftTableObject.getString("name"));
//                System.out.println("leftTable columns-->" + leftTableObject.get("columns"));
//
//                // Loop through each leftTableSortColumnsArray
//                for(int p=0; p<leftTableSortColumnsArray.length(); p++){
//                    JSONObject sortObject = leftTableSortColumnsArray.getJSONObject(p);
//
//                    String sortOrder = sortObject.getString("column");
//                    String sortOrderType = sortObject.getString("order_type");
//
//                    System.out.println("sortOrder-->" + sortOrder);
//                    System.out.println("sortOrderType-->" + sortOrderType);
//
//                }
//
//                System.out.println("leftTable generate_unique_id-->" + leftTableObject.getBoolean("generate_unique_id"));
//                System.out.println("leftTable generate_sort_id-->" + leftTableObject.getBoolean("generate_sort_id"));
//                System.out.println("leftTable filter[]-->" + leftTableObject.get("filter"));
//
//                System.out.println("Join method-->" + joinObject.getString("method"));
//
//                for (int k = 0; k < joinOnArray.length(); k++) {
//                    JSONObject joinOnObject = joinOnArray.getJSONObject(k);
//
//                    String leftColumnJoin = joinOnObject.getString("left_column");
//                    String rightColumnJoin = joinOnObject.getString("right_column");
//                    String joinCondition = joinOnObject.getString("condition");
//
//                    System.out.println("leftColumnJoin-->" + leftColumnJoin);
//                    System.out.println("rightColumnJoin-->" + rightColumnJoin);
//                    System.out.println("joinCondition-->" + joinCondition);
//
//                }
//
//                System.out.println("sort_join-->" + joinObject.getBoolean("sort_join"));
//                System.out.println("unique_join-->" + joinObject.getBoolean("unique_join"));
//
//                // Loop through each compare array in the object
//                for (int j = 0; j < compareArray.length(); j++) {
//
//                    JSONObject compareObj = compareArray.getJSONObject(j);
//                    String leftColumn = compareObj.getString("left_column");
//                    String rightColumn = compareObj.getString("right_column");
//                    String condition = compareObj.getString("condition");
//
//                    System.out.println("leftColumn compare -->" + leftColumn);
//                    System.out.println("rightColumn compare-->" + rightColumn);
//                    System.out.println("compare condition-->" + condition);
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
