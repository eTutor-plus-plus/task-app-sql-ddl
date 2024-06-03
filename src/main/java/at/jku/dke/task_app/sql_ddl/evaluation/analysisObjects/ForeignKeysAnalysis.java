package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;

import at.jku.dke.task_app.sql_ddl.evaluation.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type Foreign keys analysis.
 */
public class ForeignKeysAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<ErrorTupel> missingForeignKeys;
    private List<ErrorTupel> surplusForeignKeys;
    private List<ErrorTupel> wrongUpdateForeignKeys;
    private List<ErrorTupel> wrongDeleteForeignKeys;
    //endregion

    /**
     * Creates a new instance of class Foreign keys analysis.
     */
    public ForeignKeysAnalysis() {
        missingForeignKeys = new ArrayList<>();
        surplusForeignKeys = new ArrayList<>();
        wrongUpdateForeignKeys = new ArrayList<>();
        wrongDeleteForeignKeys = new ArrayList<>();
    }

    /**
     * Is missing foreign keys empty boolean.
     *
     * @return The boolean.
     */
    public boolean isMissingForeignKeysEmpty() {
        return this.missingForeignKeys.isEmpty();
    }

    /**
     * Is surplus foreign keys empty boolean.
     *
     * @return The boolean.
     */
    public boolean isSurplusForeignKeysEmpty() {
        return this.surplusForeignKeys.isEmpty();
    }

    /**
     * Is wrong update foreign keys empty boolean.
     *
     * @return The boolean.
     */
    public boolean isWrongUpdateForeignKeysEmpty() {
        return this.wrongUpdateForeignKeys.isEmpty();
    }

    /**
     * Is wrong delete foreign keys empty boolean.
     *
     * @return The boolean.
     */
    public boolean isWrongDeleteForeignKeysEmpty() {
        return this.wrongDeleteForeignKeys.isEmpty();
    }

    /**
     * Iter missing foreign keys iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterMissingForeignKeys() {
        return this.missingForeignKeys.iterator();
    }

    /**
     * Iter surplus foreign keys iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterSurplusForeignKeys() {
        return this.surplusForeignKeys.iterator();
    }

    /**
     * Iter wrong update foreign keys iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterWrongUpdateForeignKeys() {
        return this.wrongUpdateForeignKeys.iterator();
    }

    /**
     * Iter wrong delete foreign keys iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterWrongDeleteForeignKeys() {
        return this.wrongDeleteForeignKeys.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    /**
     * Gets the missing foreign keys.
     *
     * @return The missing foreign keys.
     */
//region Getter/Setter
    public List<ErrorTupel> getMissingForeignKeys() {
        return missingForeignKeys;
    }

    /**
     * Sets the missing foreign keys.
     *
     * @param missingForeignKeys The missing foreign keys.
     */
    public void setMissingForeignKeys(List<ErrorTupel> missingForeignKeys) {
        this.missingForeignKeys = missingForeignKeys;
    }

    /**
     * Add missing foreign key.
     *
     * @param foreignKey The foreign key.
     */
    public void addMissingForeignKey(ErrorTupel foreignKey) {
        this.missingForeignKeys.add(foreignKey);
    }

    /**
     * Gets the surplus foreign keys.
     *
     * @return The surplus foreign keys.
     */
    public List<ErrorTupel> getSurplusForeignKeys() {
        return surplusForeignKeys;
    }

    /**
     * Sets the surplus foreign keys.
     *
     * @param surplusForeignKeys The surplus foreign keys.
     */
    public void setSurplusForeignKeys(List<ErrorTupel> surplusForeignKeys) {
        this.surplusForeignKeys = surplusForeignKeys;
    }

    /**
     * Add surplus foreign key.
     *
     * @param foreignKey The foreign key.
     */
    public void addSurplusForeignKey(ErrorTupel foreignKey) {
        this.surplusForeignKeys.add(foreignKey);
    }

    /**
     * Gets the wrong update foreign keys.
     *
     * @return The wrong update foreign keys.
     */
    public List<ErrorTupel> getWrongUpdateForeignKeys() {
        return wrongUpdateForeignKeys;
    }

    /**
     * Sets the wrong update foreign keys.
     *
     * @param wrongUpdateForeignKeys The wrong update foreign keys.
     */
    public void setWrongUpdateForeignKeys(List<ErrorTupel> wrongUpdateForeignKeys) {
        this.wrongUpdateForeignKeys = wrongUpdateForeignKeys;
    }

    /**
     * Add wrong update foreign key.
     *
     * @param foreignKey The foreign key.
     */
    public void addWrongUpdateForeignKey(ErrorTupel foreignKey) {
        this.wrongUpdateForeignKeys.add(foreignKey);
    }

    /**
     * Gets the wrong delete foreign keys.
     *
     * @return The wrong delete foreign keys.
     */
    public List<ErrorTupel> getWrongDeleteForeignKeys() {
        return wrongDeleteForeignKeys;
    }

    /**
     * Sets the wrong delete foreign keys.
     *
     * @param wrongDeleteForeignKeys The wrong delete foreign keys.
     */
    public void setWrongDeleteForeignKeys(List<ErrorTupel> wrongDeleteForeignKeys) {
        this.wrongDeleteForeignKeys = wrongDeleteForeignKeys;
    }

    /**
     * Add wrong delete foreign key.
     *
     * @param foreignKey The foreign key.
     */
    public void addWrongDeleteForeignKey(ErrorTupel foreignKey) {
        this.wrongDeleteForeignKeys.add(foreignKey);
    }
    //endregion
}
