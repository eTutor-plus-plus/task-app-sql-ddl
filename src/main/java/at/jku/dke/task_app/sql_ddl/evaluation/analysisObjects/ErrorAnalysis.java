package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;

import at.jku.dke.task_app.sql_ddl.evaluation.DDLEvaluationCriterion;

/**
 * The type Error analysis.
 */
public class ErrorAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    /**
     * The Error message.
     */
    String errorMessage;

    /**
     * Creates a new instance of class Error analysis.
     *
     * @param errorMessage The error message.
     * @param isSatisfied  The is satisfied.
     */
    public ErrorAnalysis(String errorMessage, boolean isSatisfied) {
        super.setCriterionIsSatisfied(isSatisfied);
        this.errorMessage = errorMessage;
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    /**
     * Gets the error message.
     *
     * @return The error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
