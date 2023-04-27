package hello;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.StringJoiner;

public class ConstructQuery {

    private Table left_table;
    private Table right_table;
    private JoinMethod join;
    private ComparisonColumns comparisonColumns;


    //Constructor
    public ConstructQuery(JSONObject object){
        JSONObject leftTableObject = object.getJSONObject("left_table");
        Table constructedLeftTableObject = new Table(leftTableObject,true);

        JSONObject rightTableObject = object.getJSONObject("right_table");
        Table constructedRightTableObject = new Table(rightTableObject, false);

        JSONObject joinObject = object.getJSONObject("join");
        JoinMethod constructedJoinObject = new JoinMethod(joinObject);

        JSONArray compareArray = object.getJSONArray("compare");
        ComparisonColumns constructedCompareObject = new ComparisonColumns(compareArray);

        this.left_table = constructedLeftTableObject;
        this.right_table = constructedRightTableObject;
        this.join = constructedJoinObject;
        this.comparisonColumns = constructedCompareObject;
    }

    //Getters and Setters
    public Table getLeft_table() {
        return left_table;
    }

    public void setLeft_table(Table left_table) {
        this.left_table = left_table;
    }

    public Table getRight_table() {
        return right_table;
    }

    public void setRight_table(Table right_table) {
        this.right_table = right_table;
    }

    public JoinMethod getJoin() {
        return join;
    }

    public void setJoin(JoinMethod join) {
        this.join = join;
    }

    public ComparisonColumns getComparisonColumns() {
        return comparisonColumns;
    }

    public void setComparisonColumns(ComparisonColumns comparisonColumns) {
        this.comparisonColumns = comparisonColumns;
    }

    @Override
    public String toString() {
        return "GenerateQuery{" +
                "left_table=" + left_table +
                ", right_table=" + right_table +
                ", join=" + join +
                ", comparisonColumns=" + comparisonColumns +
                '}';
    }

    // Method to dynamically generated query
    public String getOutputQuery(){
        String dataQuery = "WITH \n"
                +"left_table \n"
                +"AS \n"
                +"( \n"
                + this.generateQueryForTable(this.left_table)
                + "),\n"
                + "right_table \n"
                + "AS\n"
                + "( \n"
                +this.generateQueryForTable(this.right_table)
                +") \n"
                +",final_table \n"
                +"AS \n"
                +"( \n"
                + this.generateFinalQuery(this.left_table, this.right_table,this.join,this.comparisonColumns)
                +") \n"
                +this.generateMainQuery(this.left_table, this.right_table,this.join,this.comparisonColumns);

        return dataQuery;
    }

    // Helper methods
    private String generateQueryForTable(Table tableObject){
        String dataQuery = this.getTabs(1)+"SELECT \n"
                + this.getColumnName(tableObject,false) +"\n"
                + this.generateUniqueColumn(tableObject)
                + this.generateSortColumn(tableObject)
                + this.generateDataJson(tableObject)
                + this.getTabs(1)+"FROM " + tableObject.getDatabase() + "." + tableObject.getName() +"\n";

        return dataQuery;
    }

    private String generateDataJson(Table table){
        String query = this.getTabs(2)+",CONCAT( \n"
                + this.getTabs(3)+"'{', \n"
                +this.getJsonString(table.getColumns())
                + this.getTabs(3)+"'}' ) AS data_json \n";

        return query;
    }

    private String getJsonString(String[] columns){
        StringBuilder jsonQuery = new StringBuilder("");
        for(int i=0; i<columns.length; i++){
            jsonQuery.append(this.getTabs(4)+"CONCAT('\""+columns[i]+"\":','\"', CAST("+columns[i]+" AS VARCHAR),'\"'");
            if(i == columns.length-1){
                jsonQuery.append("), \n");
            }else{
                jsonQuery.append(",','), \n");
            }
        }

        return jsonQuery.toString();
    }
    private String getColumnName(Table table, Boolean readTableIsLeft){
        String[] columns = table.getColumns();

        if(readTableIsLeft){
            StringBuilder columnQuery = new StringBuilder("");
            String alias = table.getIsLeftTable() ? "lt" : "rt";
            for(String column : columns){
                columnQuery.append(","+alias+"."+column+"\n");
            }
            return columnQuery.toString();
        }
        StringJoiner sj = new StringJoiner(", \n" + this.getTabs(2));
        for (int i = 0; i < columns.length; i++) {
            String prefix = (i == 0) ? this.getTabs(2) : "";
            sj.add(prefix + columns[i]);
        }
        return sj.toString();
    }

    private String generateUniqueColumn(Table table){
        if(!table.isGenerate_unique_id()){
            return "";
        }
        return this.getTabs(2) + ",row_number() over (partition by null order by "+ String.join(", ",table.getColumns())+" asc) as unique_id \n";
    }

    private String generateSortColumn(Table table){
        if(!table.isGenerate_sort_id()){
            return "";
        }
        return this.getTabs(2)+",row_number() over (partition by null order by "+ this.generateSortByQuery(table)+") as sort_id \n";
    }

    private String generateSortByQuery(Table table){
        SortColumn[] sc = table.getSort_columns();
        StringBuilder sortBy = new StringBuilder(" ");

        for(int i=0; i< sc.length; i++){
            sortBy.append(sc[i].getColumn() +" "+ sc[i].getOrder_type());
            if (i < sc.length - 1) {
                sortBy.append(", ");
            }
        }
        return sortBy.toString();
    }

    private String generateMainQuery(Table leftTableObject, Table rightTableObject, JoinMethod joinObject, ComparisonColumns compareColumnsArray){
        String dataQuery = "SELECT \n"
                + "    OLD_TABLE_NAME, \n"
                + "    OLD_COLUMN_NAME, \n"
                + "    OLD_COLUMN_VALUE, \n"
                + "    NEW_TABLE_NAME, \n"
                + "    NEW_COLUMN_NAME, \n"
                + "    NEW_COLUMN_VALUE, \n"
                + "    OLD_TABLE_VALUE_JSON, \n"
                + "    NEW_TABLE_VALUE_JSON, \n"
                + "    CASE \n"
                + "        WHEN ( OLD_TABLE_VALUE_JSON IS NULL AND NEW_TABLE_VALUE_JSON IS NOT NULL ) THEN CONCAT('RECORD MISSING IN ', OLD_TABLE_NAME) \n"
                + "        WHEN ( OLD_TABLE_VALUE_JSON IS NOT NULL AND NEW_TABLE_VALUE_JSON IS NULL ) THEN CONCAT('RECORD MISSING IN ', NEW_TABLE_NAME) \n"
                + "        ELSE 'DATA MISMATCH' \n"
                + "    END AS COMMENTS \n"
                + "FROM final_table \n"
                + "ORDER BY \n"
                + "    OLD_TABLE_VALUE_JSON, \n"
                + "    NEW_TABLE_VALUE_JSON, \n"
                + "    OLD_COLUMN_NAME, \n"
                + "    NEW_COLUMN_NAME \n";
        return dataQuery;
    }

    private String generateFinalQuery(Table leftTableObject, Table rightTableObject, JoinMethod joinObject, ComparisonColumns compareColumnsArray){
        StringBuilder finalQuery = new StringBuilder("");
        ComparisonColumn[] compareArray = compareColumnsArray.getComparisonColumns();
        for(int i=0; i<compareArray.length; i++){
            ComparisonColumn compareObject = compareArray[i];

            finalQuery.append(this.getTabs(1)+"SELECT \n");

            finalQuery.append(this.getTabs(2)+"'"+leftTableObject.getName()+"' AS OLD_TABLE_NAME, \n");
            finalQuery.append(this.getTabs(2)+"'"+compareObject.getColumn1()+"' AS OLD_COLUMN_NAME, \n");
            finalQuery.append(this.getTabs(2)+"CAST(lt."+compareObject.getColumn1()+" AS VARCHAR) AS OLD_COLUMN_VALUE, \n");

            finalQuery.append(this.getTabs(2)+"'"+rightTableObject.getName()+"' AS NEW_TABLE_NAME, \n");
            finalQuery.append(this.getTabs(2)+"'"+compareObject.getColumn2()+"' AS NEW_COLUMN_NAME, \n");
            finalQuery.append(this.getTabs(2)+"CAST(rt."+compareObject.getColumn2()+" AS VARCHAR) AS NEW_COLUMN_VALUE, \n");

            finalQuery.append(this.getTabs(2)+"lt.data_json AS OLD_TABLE_VALUE_JSON, \n");
            finalQuery.append(this.getTabs(2)+"rt.data_json AS NEW_TABLE_VALUE_JSON \n");

            finalQuery.append(this.getTabs(1)+"FROM left_table lt \n");
            finalQuery.append(this.getJoinQuery(joinObject));
            finalQuery.append(this.getTabs(1)+"WHERE lt."+compareObject.getColumn1()+" IS "+this.getComparisonString(compareObject.getCondition())+" DISTINCT FROM rt."+compareObject.getColumn2()+"\n");

            if(compareArray.length == 1 || i < compareArray.length-1){
                finalQuery.append(this.getTabs(1)+"UNION ALL \n");
            }
        }
        return finalQuery.toString();
    }

    private String getJoinQuery(JoinMethod joinObject){
        StringBuilder joinQuery = new StringBuilder("");
        joinQuery.append(this.getTabs(1) + joinObject.getMethod() + " JOIN right_table rt \n");
        ComparisonColumn[] join = joinObject.getJoinOn();
        for(int i=0; i<join.length; i++){
            if(i == 0){
                joinQuery.append(this.getTabs(2)+"ON lt."+join[i].getColumn1()+this.getComparisonOperator(join[i].getCondition())+"rt."+join[i].getColumn2()+"\n");
            }else {
                joinQuery.append(this.getTabs(2)+"AND lt." + join[i].getColumn1() + this.getComparisonOperator(join[i].getCondition()) + "rt." + join[i].getColumn2() + "\n");
            }
        }
        if(joinObject.isUniqueJoin()){
            joinQuery.append(this.getTabs(2)+"AND lt.unique_id = rt.unique_id \n");
        }
        if(joinObject.isSortJoin()){
            joinQuery.append(this.getTabs(2)+"AND lt.sort_id = rt.sort_id \n");
        }
        return joinQuery.toString();
    }

    private String getComparisonOperator(String comparison) {
        String operator;
        switch (comparison) {
            case "EQUAL":
                operator = " = ";
                break;
            case "NOT_EQUAL":
                operator = " <> ";
                break;
            default:
                operator = "Invalid operator";
                break;
        }
        return operator;
    }

    private String getComparisonString(String comparison) {
        String operator;
        switch (comparison) {
            case "NOT_EQUAL":
                operator = "";
                break;
            case "EQUAL":
                operator = "NOT";
                break;
            default:
                operator = "";
                break;
        }
        return operator;
    }

    private String getTabs(int numTabs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numTabs; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }
}
