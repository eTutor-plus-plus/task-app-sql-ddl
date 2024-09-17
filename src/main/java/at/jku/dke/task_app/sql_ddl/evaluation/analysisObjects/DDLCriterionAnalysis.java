package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;

import at.jku.dke.task_app.sql_ddl.evaluation.DDLEvaluationCriterion;

/**
 * The interface Ddl criterion analysis.
 */
public interface DDLCriterionAnalysis {
    /**
     * Is criterion satisfied boolean.
     *
     * @return The boolean.
     */
    boolean isCriterionSatisfied();

    /**
     * Sets the criterion is satisfied.
     *
     * @param b The b.
     */
    void setCriterionIsSatisfied(boolean b);

    /**
     * Gets the evaluation criterion.
     *
     * @return The evaluation criterion.
     */
    DDLEvaluationCriterion getEvaluationCriterion();

    /**
     * Sets the analysis exception.
     *
     * @param e The e.
     */
    void setAnalysisException(AnalysisException e);

    /**
     * Gets the analysis exception.
     *
     * @return The analysis exception.
     */
    AnalysisException getAnalysisException();
}
