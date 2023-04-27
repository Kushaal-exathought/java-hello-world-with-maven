package hello;

public class ComparisonColumn {

    private String column1;
    private String column2;
    private String condition;

    // Constructors

    public ComparisonColumn(String column1, String column2, String condition) {
        this.column1 = column1;
        this.column2 = column2;
        this.condition = condition;
    }

    // Getters and Setters
    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}