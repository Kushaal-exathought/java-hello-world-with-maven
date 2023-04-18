package hello;

public class GenerateQuery {

    private Table left_table;
    private Table right_table;
    private JoinMethod join;
    private ComparisonColumns comparisonColumns;

    public GenerateQuery(Table left_table, Table right_table, JoinMethod join, ComparisonColumns comparisonColumns) {
        this.left_table = left_table;
        this.right_table = right_table;
        this.join = join;
        this.comparisonColumns = comparisonColumns;
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
                +")"
                +this.generateMainQuery(this.left_table, this.right_table,this.join,this.comparisonColumns);

        System.out.println("dataQuery-->" + dataQuery);
        return dataQuery;
    }

    private String generateQueryForTable(Table tableObject){
       String dataQuery = "SELECT \n"
              + this.getColumnName(tableObject,false) +"\n"
               + this.generateUniqueColumn(tableObject)
               + this.generateSortColumn(tableObject)
              + "FROM " + tableObject.getDatabase() + "." + tableObject.getName() +"\n";

//        System.out.println("dataQuery--->" + dataQuery);
        return dataQuery;
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
        String dataQuery = "SELECT \n"
                + this.removeStartingDelimiter(this.getColumnName(leftTableObject, true),",")+"\n"
                + this.getColumnName(rightTableObject, true)+"\n"
                + "FROM left_table lt\n"
                + this.getJoinQuery(joinObject)+"\n"
                + this.getWhereQuery(compareColumnsArray)+"\n";

        return dataQuery;
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
