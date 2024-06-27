package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;

import at.jku.dke.task_app.sql_ddl.evaluation.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type Primary keys analysis.
 */
public class PrimaryKeysAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<ErrorTupel> missingPrimaryKeys;
    private List<ErrorTupel> surplusPrimaryKeys;
    int totalPrimaryKeys;
    //endregion


    public int getTotalPrimaryKeys() {
        return totalPrimaryKeys;
    }

    public void setTotalPrimaryKeys(int totalPrimaryKeys) {
        this.totalPrimaryKeys = totalPrimaryKeys;
    }

    /**
     * Creates a new instance of class Primary keys analysis.
     */
    public PrimaryKeysAnalysis() {
        missingPrimaryKeys = new ArrayList<>();
        surplusPrimaryKeys = new ArrayList<>();
    }

    /**
     * Is missing primary keys empty boolean.
     *
     * @return The boolean.
     */
    public boolean isMissingPrimaryKeysEmpty() {
        return this.missingPrimaryKeys.isEmpty();
    }

    /**
     * Is surplus primary keys empty boolean.
     *
     * @return The boolean.
     */
    public boolean isSurplusPrimaryKeysEmpty() {
        return this.surplusPrimaryKeys.isEmpty();
    }

    /**
     * Iter missing primary keys iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterMissingPrimaryKeys() {
        return this.missingPrimaryKeys.iterator();
    }

    /**
     * Iter surplus primary keys iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterSurplusPrimaryKeys() {
        return this.surplusPrimaryKeys.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    /**
     * Gets the missing primary keys.
     *
     * @return The missing primary keys.
     */
//region Getter/Setter
    public List<ErrorTupel> getMissingPrimaryKeys() {
        return missingPrimaryKeys;
    }

    /**
     * Sets the missing primary keys.
     *
     * @param missingPrimaryKeys The missing primary keys.
     */
    public void setMissingPrimaryKeys(List<ErrorTupel> missingPrimaryKeys) {
        this.missingPrimaryKeys = missingPrimaryKeys;
    }

    /**
     * Add missing primary key.
     *
     * @param missingPrimaryKey The missing primary key.
     */
    public void addMissingPrimaryKey(ErrorTupel missingPrimaryKey) {
        this.missingPrimaryKeys.add(missingPrimaryKey);
    }

    /**
     * Gets the surplus primary keys.
     *
     * @return The surplus primary keys.
     */
    public List<ErrorTupel> getSurplusPrimaryKeys() {
        return surplusPrimaryKeys;
    }

    /**
     * Sets the surplus primary keys.
     *
     * @param surplusPrimaryKeys The surplus primary keys.
     */
    public void setSurplusPrimaryKeys(List<ErrorTupel> surplusPrimaryKeys) {
        this.surplusPrimaryKeys = surplusPrimaryKeys;
    }

    /**
     * Add surplus primary key.
     *
     * @param surplusPrimaryKey The surplus primary key.
     */
    public void addSurplusPrimaryKey(ErrorTupel surplusPrimaryKey) {
        this.surplusPrimaryKeys.add(surplusPrimaryKey);
    }
    //endregion
}
