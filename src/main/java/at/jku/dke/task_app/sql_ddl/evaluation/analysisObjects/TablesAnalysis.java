package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;


import at.jku.dke.task_app.sql_ddl.evaluation.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type Tables analysis.
 */
public class TablesAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<String> missingTables;
    private List<String> surplusTables;
    //endregion

    /**
     * Creates a new instance of class Tables analysis.
     */
    public TablesAnalysis() {
        this.missingTables = new ArrayList<>();
        this.surplusTables = new ArrayList<>();
    }

    /**
     * Is missing tables empty boolean.
     *
     * @return The boolean.
     */
    public boolean isMissingTablesEmpty() {
        return this.missingTables.isEmpty();
    }

    /**
     * Is surplus tables empty boolean.
     *
     * @return The boolean.
     */
    public boolean isSurplusTablesEmpty() {
        return this.surplusTables.isEmpty();
    }

    /**
     * Iter missing tables iterator.
     *
     * @return The iterator.
     */
    public Iterator<String> iterMissingTables() {
        return this.missingTables.iterator();
    }

    /**
     * Iter surplus tables iterator.
     *
     * @return The iterator.
     */
    public Iterator<String> iterSurplusTables() {
        return this.surplusTables.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    /**
     * Gets the missing tables.
     *
     * @return The missing tables.
     */
//region Getter/Setter
    public List<String> getMissingTables() {
        return missingTables;
    }

    /**
     * Sets the missing tables.
     *
     * @param missingTables The missing tables.
     */
    public void setMissingTables(List<String> missingTables) {
        this.missingTables = missingTables;
    }

    /**
     * Add missing tables.
     *
     * @param missingTable The missing table.
     */
    public void addMissingTables(String missingTable) {
        this.missingTables.add(missingTable);
    }

    /**
     * Remove missing tables.
     *
     * @param tables The tables.
     */
    public void removeMissingTables(List<String> tables) {
        this.missingTables.remove(tables);
    }

    /**
     * Gets the surplus tables.
     *
     * @return The surplus tables.
     */
    public List<String> getSurplusTables() {
        return surplusTables;
    }

    /**
     * Sets the surplus tables.
     *
     * @param surplusTables The surplus tables.
     */
    public void setSurplusTables(List<String> surplusTables) {
        this.surplusTables = surplusTables;
    }

    /**
     * Add surplus table.
     *
     * @param surplusTable The surplus table.
     */
    public void addSurplusTable(String surplusTable) {
        this.surplusTables.add(surplusTable);
    }

    /**
     * Remove surplus tables.
     *
     * @param tables The tables.
     */
    public void removeSurplusTables(List<String> tables) {
        this.surplusTables.remove(tables);
    }
    //endregion
}
