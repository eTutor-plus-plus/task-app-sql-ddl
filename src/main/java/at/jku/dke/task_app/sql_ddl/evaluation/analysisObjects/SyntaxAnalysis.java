package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;

import at.jku.dke.task_app.sql_ddl.evaluation.DDLEvaluationCriterion;

/**
 * The type Syntax analysis.
 */
public class SyntaxAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private boolean foundError;
    private String errorDescription;
    //endregion

    /**
     * Creates a new instance of class Syntax analysis.
     */
    public SyntaxAnalysis() {
        this.foundError = false;
        this.errorDescription = "";
    }

    public DDLEvaluationCriterion getEvaluationCriterion() {
        return DDLEvaluationCriterion.CORRECT_SYNTAX;
    }

    /**
     * Is found error boolean.
     *
     * @return The boolean.
     */
//region Getter/Setter
    public boolean isFoundError() {
        return foundError;
    }

    /**
     * Sets the found error.
     *
     * @param foundError The found error.
     */
    public void setFoundError(boolean foundError) {
        this.foundError = foundError;
    }

    /**
     * Gets the error description.
     *
     * @return The error description.
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Sets the error description.
     *
     * @param errorDescription The error description.
     */
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
    //endregion
}
