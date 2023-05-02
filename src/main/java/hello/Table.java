package hello;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;

public class Table {

    private String database;
    private String name;
    private String[] columns;
    private Boolean isLeftTable;
    private int noOfColumns;
    private SortColumn[] sortColumns;
    private boolean generateUniqueId;
    private boolean generateSortId;
    private String[] filter;

    // Constructors

    public Table(JSONObject tableObject, Boolean isLeftTable) {
        this.database = tableObject.getString("database");
        this.name = tableObject.getString("name");
        this.isLeftTable = isLeftTable;

        JSONArray jsonArray = tableObject.getJSONArray("columns");
        String[] stringArray = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray[i] = jsonArray.getString(i);
        }

        this.columns = stringArray;
       // this.noOfColumns = this.columns.length;

        JSONArray sortColumnArray = tableObject.getJSONArray("sort_columns");
        SortColumn[] sc = new SortColumn[sortColumnArray.length()];
        for (int p = 0; p < sortColumnArray.length(); p++) {
            JSONObject sortObject = sortColumnArray.getJSONObject(p);
            String sortOrder = sortObject.getString("column");
            String sortOrderType = sortObject.getString("order_type");
            SortColumn scc = new SortColumn(sortOrder, sortOrderType);
            sc[p] = scc;
        }
        this.sortColumns = sc;

        this.generateUniqueId = tableObject.getBoolean("generate_unique_id");
        this.generateSortId = tableObject.getBoolean("generate_sort_id");

        JSONArray filterArray = tableObject.getJSONArray("filter");
        String[] filterArrayCast = new String[filterArray.length()];
        for (int i = 0; i < filterArray.length(); i++) {
            filterArrayCast[i] = filterArray.getString(i);
        }

        this.filter = filterArrayCast;
    }

    // Getters and setters
    public Boolean getIsLeftTable() {
        return isLeftTable;
    }

    public void setIsLeftTable(Boolean leftTable) {
        isLeftTable = leftTable;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public SortColumn[] getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(SortColumn[] sortColumns) {
        this.sortColumns = sortColumns;
    }

    public boolean isGenerateUniqueId() {
        return generateUniqueId;
    }

    public void setGenerateUniqueId(boolean generateUniqueId) {
        this.generateUniqueId = generateUniqueId;
    }

    public boolean isGenerateSortId() {
        return generateSortId;
    }

    public void setGenerateSortId(boolean generateSortId) {
        this.generateSortId = generateSortId;
    }

//    public int getNoOfColumns() {
//        return noOfColumns;
//    }
//
//    public void setNoOfColumns(int noOfColumns) {
//        this.noOfColumns = noOfColumns;
//    }

    public String[] getFilter() {
        return filter;
    }

    public void setFilter(String[] filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "Table{" +
                "database='" + database + '\'' +
                ", name='" + name + '\'' +
                ", columns=" + Arrays.toString(columns) +
                ", isLeftTable=" + isLeftTable +
                ", sortColumns=" + Arrays.toString(sortColumns) +
                ", generateUniqueId=" + generateUniqueId +
                ", generateSortId=" + generateSortId +
                ", filter=" + Arrays.toString(filter) +
                '}';
    }
}
