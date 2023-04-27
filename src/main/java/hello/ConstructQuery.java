package hello;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.StringJoiner;

public class ConstructQuery {

    private Table leftTable;
    private Table rightTable;
    private JoinMethod join;
    private ComparisonColumns comparisonColumns;

    public final static String newLine = "\n";
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

        this.leftTable = constructedLeftTableObject;
        this.rightTable = constructedRightTableObject;
        this.join = constructedJoinObject;
        this.comparisonColumns = constructedCompareObject;
    }

    //Getters and Setters
    public Table getLeftTable() {
        return leftTable;
    }

    public void setLeftTable(Table leftTable) {
        this.leftTable = leftTable;
    }

    public Table getRightTable() {
        return rightTable;
    }

    public void setRightTable(Table rightTable) {
        this.rightTable = rightTable;
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
                "left_table=" + leftTable +
                ", right_table=" + rightTable +
                ", join=" + join +
                ", comparisonColumns=" + comparisonColumns +
                '}';
    }

    // Method to dynamically generated query
    public String getOutputQuery(){
        String dataQuery = "WITH " + newLine
                +"left_table " + newLine
                +"AS " + newLine
                +"( " + newLine
                + this.generateQueryForTable(this.leftTable)
                + ")," + newLine
                + "right_table " + newLine
                + "AS " + newLine
                + "( " + newLine
                +this.generateQueryForTable(this.rightTable)
                +") " + newLine
                +",final_table " + newLine
                +"AS " + newLine
                +"( " + newLine
                + this.generateFinalQuery()
                +") " + newLine
                +this.generateMainQuery();

        return dataQuery;
    }

    // Helper methods
    private String generateQueryForTable(Table tableObject){
        String dataQuery = this.getTabs(1)+"SELECT " + newLine
                + this.getColumnName(tableObject) + newLine
                + this.generateUniqueColumn(tableObject)
                + this.generateSortColumn(tableObject)
                + this.generateDataJson(tableObject)
                + this.getTabs(1)+"FROM " + tableObject.getDatabase() + "." + tableObject.getName() + newLine;

        return dataQuery;
    }

    private String generateDataJson(Table table){
        String query = this.getTabs(2)+",CONCAT( " + newLine
                + this.getTabs(3)+"'{', " + newLine
                +this.getJsonString(table.getColumns())
                + this.getTabs(3)+"'}' ) AS data_json " + newLine;

        return query;
    }

    private String getJsonString(String[] columns){
        StringBuilder jsonQuery = new StringBuilder("");
        for(int i=0; i<columns.length; i++){
            jsonQuery.append(this.getTabs(4)+"CONCAT('\""+columns[i]+"\":','\"', CAST("+columns[i]+" AS VARCHAR),'\"'");
            if(i == columns.length-1){
                jsonQuery.append("), " + newLine);
            }else{
                jsonQuery.append(",','), " + newLine);
            }
        }

        return jsonQuery.toString();
    }
    private String getColumnName(Table table){
        String[] columns = table.getColumns();
        StringBuilder sb = new StringBuilder();
        String tabs = this.getTabs(2);
        for (int i = 0; i < columns.length; i++) {
            String prefix = (i == 0) ? tabs : "";
            sb.append(prefix).append(columns[i]);
            if (i < columns.length - 1) {
                sb.append(", " + newLine).append(tabs);
            }
        }
        return sb.toString();
    }

    private String generateUniqueColumn(Table table){
        if(!table.isGenerateUniqueId()){
            return "";
        }
        return this.getTabs(2) + ",row_number() over (partition by null order by "+ String.join(", ",table.getColumns())+" asc) as unique_id " + newLine;
    }

    private String generateSortColumn(Table table){
        if(!table.isGenerateSortId()){
            return "";
        }
        return this.getTabs(2)+",row_number() over (partition by null order by "+ this.generateSortByQuery(table)+") as sort_id " + newLine;
    }

    private String generateSortByQuery(Table table){
        SortColumn[] sc = table.getSortColumns();
        StringBuilder sortBy = new StringBuilder(" ");

        for(int i=0; i< sc.length; i++){
            sortBy.append(sc[i].getColumn() +" "+ sc[i].getOrderType());
            if (i < sc.length - 1) {
                sortBy.append(", ");
            }
        }
        return sortBy.toString();
    }

    private static String generateMainQuery(){
        String dataQuery = "SELECT " + newLine
                + "    OLD_TABLE_NAME, " + newLine
                + "    OLD_COLUMN_NAME, " + newLine
                + "    OLD_COLUMN_VALUE, " + newLine
                + "    NEW_TABLE_NAME, " + newLine
                + "    NEW_COLUMN_NAME, " + newLine
                + "    NEW_COLUMN_VALUE, " + newLine
                + "    OLD_TABLE_VALUE_JSON, " + newLine
                + "    NEW_TABLE_VALUE_JSON, " + newLine
                + "    CASE " + newLine
                + "        WHEN ( OLD_TABLE_VALUE_JSON IS NULL AND NEW_TABLE_VALUE_JSON IS NOT NULL ) THEN CONCAT('RECORD MISSING IN ', OLD_TABLE_NAME) " + newLine
                + "        WHEN ( OLD_TABLE_VALUE_JSON IS NOT NULL AND NEW_TABLE_VALUE_JSON IS NULL ) THEN CONCAT('RECORD MISSING IN ', NEW_TABLE_NAME) " + newLine
                + "        ELSE 'DATA MISMATCH' " + newLine
                + "    END AS COMMENTS " + newLine
                + "FROM final_table " + newLine
                + "ORDER BY " + newLine
                + "    OLD_TABLE_VALUE_JSON, " + newLine
                + "    NEW_TABLE_VALUE_JSON, " + newLine
                + "    OLD_COLUMN_NAME, " + newLine
                + "    NEW_COLUMN_NAME " + newLine;
        return dataQuery;
    }

    private String generateFinalQuery(){
        StringBuilder finalQuery = new StringBuilder("");
        ComparisonColumn[] compareArray = this.comparisonColumns.getComparisonColumns();
        for(int i=0; i<compareArray.length; i++){
            ComparisonColumn compareObject = compareArray[i];

            finalQuery.append(this.getTabs(1)+"SELECT " + newLine);

            finalQuery.append(this.getTabs(2)+"'"+this.leftTable.getName()+"' AS OLD_TABLE_NAME, " + newLine);
            finalQuery.append(this.getTabs(2)+"'"+compareObject.getColumn1()+"' AS OLD_COLUMN_NAME, " + newLine);
            finalQuery.append(this.getTabs(2)+"CAST(lt."+compareObject.getColumn1()+" AS VARCHAR) AS OLD_COLUMN_VALUE, " + newLine);

            finalQuery.append(this.getTabs(2)+"'"+this.rightTable.getName()+"' AS NEW_TABLE_NAME, " + newLine);
            finalQuery.append(this.getTabs(2)+"'"+compareObject.getColumn2()+"' AS NEW_COLUMN_NAME, " + newLine);
            finalQuery.append(this.getTabs(2)+"CAST(rt."+compareObject.getColumn2()+" AS VARCHAR) AS NEW_COLUMN_VALUE, " + newLine);

            finalQuery.append(this.getTabs(2)+"lt.data_json AS OLD_TABLE_VALUE_JSON, " + newLine);
            finalQuery.append(this.getTabs(2)+"rt.data_json AS NEW_TABLE_VALUE_JSON " + newLine);

            finalQuery.append(this.getTabs(1)+"FROM left_table lt " + newLine);
            finalQuery.append(this.getJoinQuery());
            finalQuery.append(this.getTabs(1)+"WHERE lt."+compareObject.getColumn1()+" IS "+this.getComparisonString(compareObject.getCondition())+" DISTINCT FROM rt."+compareObject.getColumn2() + newLine);

            if(compareArray.length == 1 || i < compareArray.length-1){
                finalQuery.append(this.getTabs(1)+"UNION ALL " + newLine);
            }
        }
        return finalQuery.toString();
    }

    private String getJoinQuery(){
        StringBuilder joinQuery = new StringBuilder("");
        joinQuery.append(this.getTabs(1) + this.join.getMethod() + " JOIN right_table rt " + newLine);
        ComparisonColumn[] join = this.join.getJoinOn();
        for(int i=0; i<join.length; i++){
            if(i == 0){
                joinQuery.append(this.getTabs(2)+"ON lt."+join[i].getColumn1()+this.getComparisonOperator(join[i].getCondition())+"rt."+join[i].getColumn2() + newLine);
            }else {
                joinQuery.append(this.getTabs(2)+"AND lt." + join[i].getColumn1() + this.getComparisonOperator(join[i].getCondition()) + "rt." + join[i].getColumn2() + newLine);
            }
        }
        if(this.join.isUniqueJoin()){
            joinQuery.append(this.getTabs(2)+"AND lt.unique_id = rt.unique_id " + newLine);
        }
        if(this.join.isSortJoin()){
            joinQuery.append(this.getTabs(2)+"AND lt.sort_id = rt.sort_id "+newLine);
        }
        return joinQuery.toString();
    }

    private static String getComparisonOperator(String comparison) {
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

    private static String getComparisonString(String comparison) {
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

    private static String getTabs(int numTabs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numTabs; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }
}
