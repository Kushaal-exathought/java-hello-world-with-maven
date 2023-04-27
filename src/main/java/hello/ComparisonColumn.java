package hello;

enum CompareCondition {
    EQUAL,
    NOT_EQUAL
}

public class ComparisonColumn {

    private String column1;
    private String column2;
    private CompareCondition condition;

    // Constructors

    public ComparisonColumn(String column1, String column2, String condition) {
        this.column1 = column1;
        this.column2 = column2;
        CompareCondition compareCondition = CompareCondition.valueOf(condition.toUpperCase());
        this.condition = compareCondition;
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


    public CompareCondition getCondition() {
        return condition;
    }

    public void setCondition(CompareCondition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "ComparisonColumn{" +
                "column1='" + column1 + '\'' +
                ", column2='" + column2 + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}