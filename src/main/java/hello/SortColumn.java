package hello;

public  class SortColumn {
    private String column;
    private String order_type;

    // Constructors
    public SortColumn() {}

    public SortColumn(String column, String order_type) {
        this.column = column;
        this.order_type = order_type;
    }

    // Getters and setters
    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }
}






