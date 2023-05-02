package hello;

import org.json.JSONObject;

public class ConstructQuery {

    private Table leftTable;
    private Table rightTable;
    private JoinMethod join;
    private ComparisonColumns comparisonColumns;

    //Static variable
    public static final String NEW_LINE = "\n";
    public static final String EQUAL = "EQUAL";
    public static final String NOT_EQUAL = "NOT_EQUAL";
    public static final String NOT = "NOT";

    //Constructor
    public ConstructQuery(JSONObject object) {
        this.leftTable = new Table(object.getJSONObject("left_table"));
        this.rightTable = new Table(object.getJSONObject("right_table"));
        this.join = new JoinMethod(object.getJSONObject("join"));
        this.comparisonColumns = new ComparisonColumns(object.getJSONArray("compare"));
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
    public String getOutputQuery() {
        String dataQuery = "WITH " + NEW_LINE
                + "left_table " + NEW_LINE
                + "AS " + NEW_LINE
                + "( " + NEW_LINE
                + generateQueryForTable(this.leftTable)
                + ")," + NEW_LINE
                + "right_table " + NEW_LINE
                + "AS " + NEW_LINE
                + "( " + NEW_LINE
                + generateQueryForTable(this.rightTable)
                + ") " + NEW_LINE
                + ",final_table " + NEW_LINE
                + "AS " + NEW_LINE
                + "( " + NEW_LINE
                + generateColumnCompareQuery()
                + ") " + NEW_LINE
                + generateMainQuery();

        return dataQuery;
    }

    // Helper methods
    private static String generateQueryForTable(Table tableObject) {
        return getTabs(1) + "SELECT " + NEW_LINE
                + getColumnName(tableObject) + NEW_LINE
                + generateUniqueColumn(tableObject)
                + generateSortColumn(tableObject)
                + generateDataJson(tableObject)
                + getTabs(1) + "FROM " + tableObject.getDatabase() + "." + tableObject.getName() + NEW_LINE;
    }

    private static String generateDataJson(Table table) {
        return getTabs(2) + ",CONCAT( " + NEW_LINE
                + getTabs(3) + "'{', " + NEW_LINE
                + getJsonString(table.getColumns())
                + getTabs(3) + "'}' ) AS data_json " + NEW_LINE;
    }

    private static String getJsonString(String[] columns) {
        StringBuilder jsonQuery = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            jsonQuery.append(getTabs(4) + "CONCAT('\"" + columns[i] + "\":','\"', CAST(" + columns[i] + " AS VARCHAR),'\"'");
            if (i == columns.length - 1) {
                jsonQuery.append("), " + NEW_LINE);
            } else {
                jsonQuery.append(",','), " + NEW_LINE);
            }
        }

        return jsonQuery.toString();
    }

    private static String getColumnName(Table table) {
        String[] columns = table.getColumns();
        StringBuilder sb = new StringBuilder();
        String tabs = getTabs(2);
        for (int i = 0; i < columns.length; i++) {
            String prefix = (i == 0) ? tabs : "";
            sb.append(prefix).append(columns[i]);
            if (i < columns.length - 1) {
                sb.append(", " + NEW_LINE).append(tabs);
            }
        }
        return sb.toString();
    }

    private static String generateUniqueColumn(Table table) {
        if (!table.isGenerateUniqueId()) {
            return "";
        }
        return getTabs(2) + ",row_number() over (partition by null order by " + String.join(", ", table.getColumns()) + " asc) as unique_id " + NEW_LINE;
    }

    private static String generateSortColumn(Table table) {
        if (!table.isGenerateSortId()) {
            return "";
        }
        return getTabs(2) + ",row_number() over (partition by null order by " + generateSortByQuery(table) + ") as sort_id " + NEW_LINE;
    }

    private static String generateSortByQuery(Table table) {
        SortColumn[] sc = table.getSortColumns();
        StringBuilder sortBy = new StringBuilder(" ");
        for (int i = 0; i < sc.length; i++) {
            sortBy.append(sc[i].getColumn() + " " + sc[i].getOrderType());
            if (i < sc.length - 1) {
                sortBy.append(", ");
            }
        }
        return sortBy.toString();
    }

    private String generateColumnCompareQuery() {
        StringBuilder finalQuery = new StringBuilder();
        ComparisonColumn[] compareArray = this.comparisonColumns.getComparisonColumns();
        for (int i = 0; i < compareArray.length; i++) {
            ComparisonColumn compareObject = compareArray[i];

            finalQuery.append(getTabs(1) + "SELECT " + NEW_LINE)
                    .append(getTabs(2) + "'" + this.leftTable.getName() + "' AS OLD_TABLE_NAME, " + NEW_LINE)
                    .append(getTabs(2) + "'" + compareObject.getColumn1() + "' AS OLD_COLUMN_NAME, " + NEW_LINE)
                    .append(getTabs(2) + "CAST(lt." + compareObject.getColumn1() + " AS VARCHAR) AS OLD_COLUMN_VALUE, " + NEW_LINE)
                    .append(getTabs(2) + "'" + this.rightTable.getName() + "' AS NEW_TABLE_NAME, " + NEW_LINE)
                    .append(getTabs(2) + "'" + compareObject.getColumn2() + "' AS NEW_COLUMN_NAME, " + NEW_LINE)
                    .append(getTabs(2) + "CAST(rt." + compareObject.getColumn2() + " AS VARCHAR) AS NEW_COLUMN_VALUE, " + NEW_LINE)
                    .append(getTabs(2) + "lt.data_json AS OLD_TABLE_VALUE_JSON, " + NEW_LINE)
                    .append(getTabs(2) + "rt.data_json AS NEW_TABLE_VALUE_JSON " + NEW_LINE)
                    .append(getTabs(1) + "FROM left_table lt " + NEW_LINE)
                    .append(getJoinQuery())
                    .append(getTabs(1) + "WHERE lt." + compareObject.getColumn1() + " IS " + getComparisonString(compareObject.getCondition().toString()) + " DISTINCT FROM rt." + compareObject.getColumn2() + NEW_LINE);

            if (compareArray.length == 1 || i < compareArray.length - 1) {
                finalQuery.append(getTabs(1) + "UNION ALL " + NEW_LINE);
            }
        }
        return finalQuery.toString();
    }

    private String getJoinQuery() {
        StringBuilder joinQuery = new StringBuilder();
        joinQuery.append(getTabs(1) + this.join.getMethod().toString() + " JOIN right_table rt " + NEW_LINE);
        ComparisonColumn[] join = this.join.getOn();
        for (int i = 0; i < join.length; i++) {
            if (i == 0) {
                joinQuery.append(getTabs(2) + "ON lt." + join[i].getColumn1() + getComparisonOperator(join[i].getCondition().toString()) + "rt." + join[i].getColumn2() + NEW_LINE);
            } else {
                joinQuery.append(getTabs(2) + "AND lt." + join[i].getColumn1() + getComparisonOperator(join[i].getCondition().toString()) + "rt." + join[i].getColumn2() + NEW_LINE);
            }
        }
        if (this.join.isUniqueJoin()) {
            joinQuery.append(getTabs(2) + "AND lt.unique_id = rt.unique_id " + NEW_LINE);
        }
        if (this.join.isSortJoin()) {
            joinQuery.append(getTabs(2) + "AND lt.sort_id = rt.sort_id " + NEW_LINE);
        }
        return joinQuery.toString();
    }

    //Static Methods
    private static String generateMainQuery() {
        return "SELECT " + NEW_LINE
                + "    OLD_TABLE_NAME, " + NEW_LINE
                + "    OLD_COLUMN_NAME, " + NEW_LINE
                + "    OLD_COLUMN_VALUE, " + NEW_LINE
                + "    NEW_TABLE_NAME, " + NEW_LINE
                + "    NEW_COLUMN_NAME, " + NEW_LINE
                + "    NEW_COLUMN_VALUE, " + NEW_LINE
                + "    OLD_TABLE_VALUE_JSON, " + NEW_LINE
                + "    NEW_TABLE_VALUE_JSON, " + NEW_LINE
                + "    CASE " + NEW_LINE
                + "        WHEN ( OLD_TABLE_VALUE_JSON IS NULL AND NEW_TABLE_VALUE_JSON IS NOT NULL ) THEN CONCAT('RECORD MISSING IN ', OLD_TABLE_NAME) " + NEW_LINE
                + "        WHEN ( OLD_TABLE_VALUE_JSON IS NOT NULL AND NEW_TABLE_VALUE_JSON IS NULL ) THEN CONCAT('RECORD MISSING IN ', NEW_TABLE_NAME) " + NEW_LINE
                + "        ELSE 'DATA MISMATCH' " + NEW_LINE
                + "    END AS COMMENTS " + NEW_LINE
                + "FROM final_table " + NEW_LINE
                + "ORDER BY " + NEW_LINE
                + "    OLD_TABLE_VALUE_JSON, " + NEW_LINE
                + "    NEW_TABLE_VALUE_JSON, " + NEW_LINE
                + "    OLD_COLUMN_NAME, " + NEW_LINE
                + "    NEW_COLUMN_NAME " + NEW_LINE;
    }

    private static String getComparisonOperator(String comparison) {
        switch (comparison) {
            case NOT_EQUAL:
                return " <> ";
            default:
                return " = ";
        }
    }

    private static String getComparisonString(String comparison) {
        switch (comparison) {
            case EQUAL:
                return NOT;
            default:
                return "";
        }
    }

    private static String getTabs(int numTabs) {
        return "\t".repeat(numTabs);
    }
}
