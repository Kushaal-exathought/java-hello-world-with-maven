package hello;

public class Table {
    private String database;
    private String name;
    private String[] columns;
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

    public String[] getFilter() {
        return filter;
    }

    public void setFilter(String[] filter) {
        this.filter = filter;
    }
}
