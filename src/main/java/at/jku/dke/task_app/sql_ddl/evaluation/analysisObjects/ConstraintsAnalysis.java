package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;

import at.jku.dke.task_app.sql_ddl.evaluation.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type Constraints analysis.
 */
public class ConstraintsAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private boolean insertStatementsChecked;
    private List<ErrorTupel> missingConstraints;
    private List<ErrorTupel> surplusConstraints;
    private List<String> dmlStatementsWithMistakes;
    //endregion

    /**
     * Creates a new instance of class Constraints analysis.
     */
    public ConstraintsAnalysis() {
        missingConstraints = new ArrayList<>();
        surplusConstraints = new ArrayList<>();
        dmlStatementsWithMistakes = new ArrayList<>();
        insertStatementsChecked = false;
    }

    /**
     * Is insert statements checked boolean.
     *
     * @return The boolean.
     */
    public boolean isInsertStatementsChecked() {
        return insertStatementsChecked;
    }

    /**
     * Sets the insert statements checked.
     *
     * @param insertStatementsChecked The insert statements checked.
     */
    public void setInsertStatementsChecked(boolean insertStatementsChecked) {
        this.insertStatementsChecked = insertStatementsChecked;
    }

    /**
     * Is missing constraints empty boolean.
     *
     * @return The boolean.
     */
    public boolean isMissingConstraintsEmpty() {
        return this.missingConstraints.isEmpty();
    }

    /**
     * Is surplus constraints empty boolean.
     *
     * @return The boolean.
     */
    public boolean isSurplusConstraintsEmpty() {
        return this.surplusConstraints.isEmpty();
    }

    /**
     * Is dml statements with mistakes empty boolean.
     *
     * @return The boolean.
     */
    public boolean isDmlStatementsWithMistakesEmpty() {
        return this.dmlStatementsWithMistakes.isEmpty();
    }

    /**
     * Iter missing constraints iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterMissingConstraints() {
        return this.missingConstraints.iterator();
    }

    /**
     * Iter surplus constraints iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterSurplusConstraints() {
        return this.surplusConstraints.iterator();
    }

    /**
     * Iter dml statements with mistakes iterator.
     *
     * @return The iterator.
     */
    public Iterator<String> iterDmlStatementsWithMistakes() {
        return this.dmlStatementsWithMistakes.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    /**
     * Gets the missing constraints.
     *
     * @return The missing constraints.
     */
//region Getter/Setter
    public List<ErrorTupel> getMissingConstraints() {
        return missingConstraints;
    }

    /**
     * Sets the missing constraints.
     *
     * @param missingConstraints The missing constraints.
     */
    public void setMissingConstraints(List<ErrorTupel> missingConstraints) {
        this.missingConstraints = missingConstraints;
    }

    /**
     * Add missing constraint.
     *
     * @param missingConstraint The missing constraint.
     */
    public void addMissingConstraint(ErrorTupel missingConstraint) {
        this.missingConstraints.add(missingConstraint);
    }

    /**
     * Gets the surplus constraints.
     *
     * @return The surplus constraints.
     */
    public List<ErrorTupel> getSurplusConstraints() {
        return surplusConstraints;
    }

    /**
     * Sets the surplus constraints.
     *
     * @param surplusConstraints The surplus constraints.
     */
    public void setSurplusConstraints(List<ErrorTupel> surplusConstraints) {
        this.surplusConstraints = surplusConstraints;
    }

    /**
     * Add surplus constraint.
     *
     * @param surplusConstraint The surplus constraint.
     */
    public void addSurplusConstraint(ErrorTupel surplusConstraint) {
        this.surplusConstraints.add(surplusConstraint);
    }

    /**
     * Gets the dml statements with mistakes.
     *
     * @return The dml statements with mistakes.
     */
    public List<String> getDmlStatementsWithMistakes() {
        return dmlStatementsWithMistakes;
    }

    /**
     * Sets the dml statements with mistakes.
     *
     * @param dmlStatementsWithMistakes The dml statements with mistakes.
     */
    public void setDmlStatementsWithMistakes(List<String> dmlStatementsWithMistakes) {
        this.dmlStatementsWithMistakes = dmlStatementsWithMistakes;
    }

    /**
     * Add dml statement with mistake.
     *
     * @param stmt The stmt.
     */
    public void addDmlStatementWithMistake(String stmt) {
        this.dmlStatementsWithMistakes.add(stmt);
    }
    //endregion
}
