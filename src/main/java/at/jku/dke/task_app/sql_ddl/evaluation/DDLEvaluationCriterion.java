package at.jku.dke.task_app.sql_ddl.evaluation;

/**
 * The type Ddl evaluation criterion.
 */
public class DDLEvaluationCriterion {
    /**
     * The constant CORRECT_TABLES.
     */
//region Constants
    public static final DDLEvaluationCriterion CORRECT_TABLES = new DDLEvaluationCriterion("CORRECT TABLES");
    /**
     * The constant CORRECT_COLUMNS.
     */
    public static final DDLEvaluationCriterion CORRECT_COLUMNS = new DDLEvaluationCriterion("CORRECT COLUMNS");
    /**
     * The constant CORRECT_PRIMARY_KEYS.
     */
    public static final DDLEvaluationCriterion CORRECT_PRIMARY_KEYS = new DDLEvaluationCriterion("CORRECT PRIMARY KEYS");
    /**
     * The constant CORRECT_FOREIGN_KEYS.
     */
    public static final DDLEvaluationCriterion CORRECT_FOREIGN_KEYS = new DDLEvaluationCriterion("CORRECT FOREIGN KEYS");
    /**
     * The constant CORRECT_CONSTRAINTS.
     */
    public static final DDLEvaluationCriterion CORRECT_CONSTRAINTS = new DDLEvaluationCriterion("CORRECT CONSTRAINTS");
    /**
     * The constant CORRECT_SYNTAX.
     */
    public static final DDLEvaluationCriterion CORRECT_SYNTAX = new DDLEvaluationCriterion("CORRECT SYNTAX");
    /**
     * The constant ERROR.
     */
    public static final DDLEvaluationCriterion ERROR = new DDLEvaluationCriterion("ERROR");
    //endregion

    //region Fields
    private final String name;
    //endregion

    /**
     * Creates a new instance of class Ddl evaluation criterion.
     *
     * @param name The name.
     */
    protected DDLEvaluationCriterion(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;


        if (!(obj instanceof DDLEvaluationCriterion))
            return false;

        return this.name.equals(obj.toString());
    }

    @Override
    public String toString() {
        return this.name;
    }
}
