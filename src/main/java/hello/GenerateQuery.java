package hello;

import org.json.JSONArray;
import org.json.JSONObject;

public class GenerateQuery {

    private Table left_table;
    private Table right_table;
    private JoinMethod join;
    private ComparisonColumns comparisonColumns;

    private String[] commonColumns;

    public GenerateQuery(Table left_table, Table right_table, JoinMethod join, ComparisonColumns comparisonColumns) {
        this.left_table = left_table;
        this.right_table = right_table;
        this.join = join;
        this.comparisonColumns = comparisonColumns;
    }

    public GenerateQuery(JSONObject obj){
        JSONObject leftTableObject = obj.getJSONObject("left_table");
        Table constructedLeftTableObject = new Table(leftTableObject,true);

        JSONObject rightTableObject = obj.getJSONObject("right_table");
        Table constructedRightTableObject = new Table(rightTableObject, false);

        JSONObject joinObject = obj.getJSONObject("join");
        JoinMethod constructedJoinObject = new JoinMethod(joinObject);

        JSONArray compareArray = obj.getJSONArray("compare");
        ComparisonColumns constructedCompareObject = new ComparisonColumns(compareArray);

        this.left_table = constructedLeftTableObject;
        this.right_table = constructedRightTableObject;
        this.join = constructedJoinObject;
        this.comparisonColumns = constructedCompareObject;
    }


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

    public String getOutputQuery(){
        System.out.println("Inside getOutputQuery" + this.toString());
        String leftTable = this.generateQueryForTable(this.left_table);
        String rightTable = this.generateQueryForTable(this.right_table);
        String mainQuery = this.generateMainQuery(this.left_table, this.right_table,this.join,this.comparisonColumns);
//        System.out.println("leftTable-->" + leftTable);
//        System.out.println("rightTable-->" + rightTable);
//        System.out.println("mainQuery-->" + mainQuery);
        String dataQuery = "WITH left_table AS (\n"
                + this.generateQueryForTable(this.left_table)
                + "),\n"
                + "right_table \n"
                + "AS\n"
                + "("
                +this.generateQueryForTable(this.right_table)
                +") \n"
                +",final_table \n"
                +"AS \n"
                +"( \n"
                + this.generateFinalQuery(this.left_table, this.right_table,this.join,this.comparisonColumns)
                +") \n"
                +this.generateMainQuery(this.left_table, this.right_table,this.join,this.comparisonColumns);

        System.out.println("dataQuery-->" + dataQuery);
        return dataQuery;
    }

    private String generateQueryForTable(Table tableObject){
       String dataQuery = "SELECT \n"
              + this.getColumnName(tableObject,false) +"\n"
               + this.generateUniqueColumn(tableObject)
               + this.generateSortColumn(tableObject)
               + this.generateDataJson(tableObject)
              + "FROM " + tableObject.getDatabase() + "." + tableObject.getName() +"\n";

//        System.out.println("dataQuery--->" + dataQuery);
        return dataQuery;
    }

    private String generateDataJson(Table table){
       String query = ",CONCAT( \n"
               + "'{', \n"
               +this.getJsonString(table.getColumns())
               + "'}'  ) AS data_json \n";
       return query;
    }
     private String getJsonString(String[] columns){
         StringBuilder jsonQuery = new StringBuilder("");
        for(int i=0; i<columns.length; i++){
//            jsonQuery.append("CONCAT('\""+columns[i]+"\":', CAST("+columns[i]+" AS VARCHAR)");
            jsonQuery.append("CONCAT('\""+columns[i]+"\":','\"', CAST("+columns[i]+" AS VARCHAR),'\"'");
            if(i == columns.length-1){
                jsonQuery.append("), \n");
            }else{
                jsonQuery.append(",','), \n");
            }

        }
//        String.join("",columns);
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
//            if(columnQuery.lastIndexOf())
//            return String.join(", \n"+alias+".",columns);
            return columnQuery.toString();
        }

        return String.join(", \n",columns);
    }
    private String generateUniqueColumn(Table table){
        if(!table.isGenerate_unique_id()){
            return "";
        }
//        row_number() over (partition by null order by id, label, client_id, campaign_id asc) as unique_id,
        return ",row_number() over (partition by null order by "+ String.join(", ",table.getColumns())+" asc) as unique_id";
    }

    private String generateSortColumn(Table table){
        if(!table.isGenerate_sort_id()){
            return "";
        }
//        row_number() over (partition by null order by id asc, label desc, client_id asc, campaign_id asc) as sort_id,
        return ",row_number() over (partition by null order by "+ this.generateSortByQuery(table)+") as sort_id";
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
//        String dataQuery = "SELECT \n"
//                + this.removeStartingDelimiter(this.getColumnName(leftTableObject, true),",")+"\n"
//                + this.getColumnName(rightTableObject, true)+"\n"
//                + "FROM left_table lt\n"
//                + this.getJoinQuery(joinObject)+"\n"
//                + this.getWhereQuery(compareColumnsArray)+"\n";

        String dataQuery =  "SELECT \n"
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

            finalQuery.append("SELECT \n");

            finalQuery.append("'"+leftTableObject.getName()+"' AS OLD_TABLE_NAME, \n");
            finalQuery.append("'"+compareObject.getColumn1()+"' AS OLD_COLUMN_NAME, \n");
            finalQuery.append("CAST(lt."+compareObject.getColumn1()+" AS VARCHAR) AS OLD_COLUMN_VALUE, \n");

            finalQuery.append("'"+rightTableObject.getName()+"' AS NEW_TABLE_NAME, \n");
            finalQuery.append("'"+compareObject.getColumn2()+"' AS NEW_COLUMN_NAME, \n");
            finalQuery.append("CAST(lt."+compareObject.getColumn2()+" AS VARCHAR) AS NEW_COLUMN_VALUE, \n");

            finalQuery.append("lt.data_json AS OLD_TABLE_VALUE_JSON, \n");
            finalQuery.append("rt.data_json AS NEW_TABLE_VALUE_JSON \n");

            finalQuery.append("FROM left_table lt \n");
            finalQuery.append(this.getJoinQuery(joinObject));
            finalQuery.append("WHERE lt."+compareObject.getColumn1()+" IS "+this.getComparisonString(compareObject.getCondition())+" DISTINCT FROM rt."+compareObject.getColumn2()+"\n");

            if(compareArray.length == 1 || i < compareArray.length-1){
                finalQuery.append("UNION ALL \n");
            }
        }

        return finalQuery.toString();
    }

    private String removeStartingDelimiter(String query, String delimiter){
//        if(query.startsWith(delimiter)){
//            return query.substring(1);
//        }
//        return query;
        return query.startsWith(delimiter) ? query.substring(1) : query;
    }
    private String getJoinQuery(JoinMethod joinObject){
        StringBuilder joinQuery = new StringBuilder("");
        joinQuery.append(joinObject.getMethod() + " JOIN right_table rt \n");

        ComparisonColumn[] join = joinObject.getJoinOn();
        for(int i=0; i<join.length; i++){
            if(i == 0){
                joinQuery.append("ON lt."+join[i].getColumn1()+this.getComparisonOperator(join[i].getCondition())+"rt."+join[i].getColumn2()+"\n");
            }else {

                joinQuery.append("AND lt." + join[i].getColumn1() + this.getComparisonOperator(join[i].getCondition()) + "rt." + join[i].getColumn2() + "\n");
            }
        }
            if(joinObject.isUniqueJoin()){
                joinQuery.append("AND lt.unique_id = rt.unique_id \n");
            }

         if(joinObject.isSortJoin()){
             joinQuery.append("AND lt.sort_id = rt.sort_id \n");
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
    private String getWhereQuery(ComparisonColumns compareColumnsArray){
        StringBuilder whereQuery = new StringBuilder("WHERE ( \n");
        ComparisonColumn[] compareArray = compareColumnsArray.getComparisonColumns();
        for(int i=0; i<compareArray.length; i++){
            whereQuery.append("(lt."+compareArray[i].getColumn1()+" IS "+this.getComparisonString(compareArray[i].getCondition())+" DISTINCT FROM rt."+compareArray[i].getColumn2()+")\n");
            if(i < compareArray.length -1){
                whereQuery.append("OR \n");
            }
        }
        whereQuery.append(")");
        return whereQuery.toString();
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
}
