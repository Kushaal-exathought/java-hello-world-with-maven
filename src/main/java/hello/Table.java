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
    private SortColumn[] sort_columns;
    private boolean generate_unique_id;
    private boolean generate_sort_id;
    private String[] filter;

    // Constructors
    public Table() {}

    public Table(String database, String name, String[] columns, SortColumn[] sort_columns, boolean generate_unique_id, boolean generate_sort_id, String[] filter) {
        this.database = database;
        this.name = name;
        this.columns = columns;
        this.sort_columns = sort_columns;
        this.generate_unique_id = generate_unique_id;
        this.generate_sort_id = generate_sort_id;
        this.filter = filter;
    }

    public Table(JSONObject tableObject, Boolean isLeftTable){
        this.database = tableObject.getString("database");
        this.name = tableObject.getString("name");
        this.isLeftTable = isLeftTable;

        JSONArray jsonArray =  tableObject.getJSONArray("columns");
        String[] stringArray = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray[i] = jsonArray.getString(i);
        }
//        String[] stringArray = (String[]) Arrays.copyOf(objectArray, objectArray.length, String[].class);
        this.columns = stringArray;
        this.noOfColumns = this.columns.length;

        JSONArray sortColumnArray = tableObject.getJSONArray("sort_columns");
        SortColumn[] sc = new SortColumn[sortColumnArray.length()];
        for(int p=0; p<sortColumnArray.length(); p++){
            JSONObject sortObject = sortColumnArray.getJSONObject(p);
            String sortOrder = sortObject.getString("column");
            String sortOrderType = sortObject.getString("order_type");
            SortColumn scc = new SortColumn(sortOrder,sortOrderType);
            sc[p] = scc;
        }
        this.sort_columns = sc;

        this.generate_unique_id = tableObject.getBoolean("generate_unique_id");
        this.generate_sort_id = tableObject.getBoolean("generate_sort_id");

        JSONArray filterArray =  tableObject.getJSONArray("filter");
        String[] filterArrayCast = new String[filterArray.length()];
        for (int i = 0; i < filterArray.length(); i++) {
            filterArrayCast[i] = filterArray.getString(i);
        }
//        String[] filterStringArray = Arrays.copyOf(filterArray, filterArray.length, String[].class);
        this.filter = filterArrayCast;
    }

    public Boolean getIsLeftTable() {
        return isLeftTable;
    }

    public void setIsLeftTable(Boolean leftTable) {
        isLeftTable = leftTable;
    }

    // Getters and setters
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

    public SortColumn[] getSort_columns() {
        return sort_columns;
    }

    public void setSort_columns(SortColumn[] sort_columns) {
        this.sort_columns = sort_columns;
    }

    public boolean isGenerate_unique_id() {
        return generate_unique_id;
    }

    public void setGenerate_unique_id(boolean generate_unique_id) {
        this.generate_unique_id = generate_unique_id;
    }

    public boolean isGenerate_sort_id() {
        return generate_sort_id;
    }

    public void setGenerate_sort_id(boolean generate_sort_id) {
        this.generate_sort_id = generate_sort_id;
    }

    public int getNoOfColumns() {
        return noOfColumns;
    }

    public void setNoOfColumns(int noOfColumns) {
        this.noOfColumns = noOfColumns;
    }

    public String[] getFilter() {
        return filter;
    }

    public void setFilter(String[] filter) {
        this.filter = filter;
    }
}
