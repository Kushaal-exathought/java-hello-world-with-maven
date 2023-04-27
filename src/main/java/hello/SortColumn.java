package hello;

public  class SortColumn {

    private String column;
    private String orderType;

    // Constructors
    public SortColumn() {}

    public SortColumn(String column, String orderType) {
        this.column = column;
        this.orderType = orderType;
    }

    // Getters and setters
    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "SortColumn{" +
                "column='" + column + '\'' +
                ", orderType='" + orderType + '\'' +
                '}';
    }
}





