package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ColumnsOfTable{
    //region Fields
    private List<ErrorTupel> missingColumns;
    private List<ErrorTupel> surplusColumns;
    private List<ErrorTupel> wrongDatatypeColumns;
    private List<ErrorTupel> wrongDefaultColumns;
    private List<ErrorTupel> wrongNullColumns;
    private String tableName;
    //endregion

    /**
     * Creates a new instance of class Columns analysis.
     */
    public ColumnsOfTable() {
        this.missingColumns = new ArrayList<>();
        this.surplusColumns = new ArrayList<>();
        this.wrongDatatypeColumns = new ArrayList<>();
        this.wrongDefaultColumns = new ArrayList<>();
        this.wrongNullColumns = new ArrayList<>();
        tableName="";
    }

    public ColumnsOfTable(String tableName) {
        this.missingColumns = new ArrayList<>();
        this.surplusColumns = new ArrayList<>();
        this.wrongDatatypeColumns = new ArrayList<>();
        this.wrongDefaultColumns = new ArrayList<>();
        this.wrongNullColumns = new ArrayList<>();
        this.tableName=tableName;
    }

    /**
     * Is missing columns empty boolean.
     *
     * @return The boolean.
     */
    public boolean isMissingColumnsEmpty() {
        return this.missingColumns.isEmpty();
    }

    /**
     * Is surplus columns empty boolean.
     *
     * @return The boolean.
     */
    public boolean isSurplusColumnsEmpty() {
        return this.surplusColumns.isEmpty();
    }

    /**
     * Is wrong datatype columns empty boolean.
     *
     * @return The boolean.
     */
    public boolean isWrongDatatypeColumnsEmpty() {
        return this.wrongDatatypeColumns.isEmpty();
    }

    /**
     * Is wrong default columns empty boolean.
     *
     * @return The boolean.
     */
    public boolean isWrongDefaultColumnsEmpty() {
        return this.wrongDefaultColumns.isEmpty();
    }

    /**
     * Is wrong null columns empty boolean.
     *
     * @return The boolean.
     */
    public boolean isWrongNullColumnsEmpty() {
        return this.wrongNullColumns.isEmpty();
    }

    /**
     * Iter missing columns iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterMissingColumns() {
        return this.missingColumns.iterator();
    }

    /**
     * Iter surplus columns iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterSurplusColumns() {
        return this.surplusColumns.iterator();
    }

    /**
     * Iter wrong datatype columns iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterWrongDatatypeColumns() {
        return this.wrongDatatypeColumns.iterator();
    }

    /**
     * Iter wrong default columns iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterWrongDefaultColumns() {
        return this.wrongDefaultColumns.iterator();
    }

    /**
     * Iter wrong null columns iterator.
     *
     * @return The iterator.
     */
    public Iterator<ErrorTupel> iterWrongNullColumns() {
        return this.wrongNullColumns.iterator();
    }

    /**
     * Gets the missing columns.
     *
     * @return The missing columns.
     */
//region Getter/Setter
    public List<ErrorTupel> getMissingColumns() {
        return missingColumns;
    }

    /**
     * Sets the missing columns.
     *
     * @param missingColumns The missing columns.
     */
    public void setMissingColumns(List<ErrorTupel> missingColumns) {
        this.missingColumns = missingColumns;
    }

    /**
     * Remove missing columns.
     *
     * @param columns The columns.
     */
    public void removeMissingColumns(List<ErrorTupel> columns) {
        this.missingColumns.remove(columns);
    }

    /**
     * Add missing column.
     *
     * @param column The column.
     */
    public void addMissingColumn(ErrorTupel column) {
        this.missingColumns.add(column);
    }

    /**
     * Gets the surplus columns.
     *
     * @return The surplus columns.
     */
    public List<ErrorTupel> getSurplusColumns() {
        return surplusColumns;
    }

    /**
     * Sets the surplus columns.
     *
     * @param surplusColumns The surplus columns.
     */
    public void setSurplusColumns(List<ErrorTupel> surplusColumns) {
        this.surplusColumns = surplusColumns;
    }

    /**
     * Remove surplus columns.
     *
     * @param columns The columns.
     */
    public void removeSurplusColumns(List<ErrorTupel> columns) {
        this.surplusColumns.remove(columns);
    }

    /**
     * Add surplus column.
     *
     * @param column The column.
     */
    public void addSurplusColumn(ErrorTupel column) {
        this.surplusColumns.add(column);
    }

    /**
     * Gets the wrong datatype columns.
     *
     * @return The wrong datatype columns.
     */
    public List<ErrorTupel> getWrongDatatypeColumns() {
        return wrongDatatypeColumns;
    }

    /**
     * Sets the wrong datatype columns.
     *
     * @param wrongDatatypeColumns The wrong datatype columns.
     */
    public void setWrongDatatypeColumns(List<ErrorTupel> wrongDatatypeColumns) {
        this.wrongDatatypeColumns = wrongDatatypeColumns;
    }

    /**
     * Add wrong datatype column.
     *
     * @param column The column.
     */
    public void addWrongDatatypeColumn(ErrorTupel column) {
        this.wrongDatatypeColumns.add(column);
    }

    /**
     * Gets the wrong default columns.
     *
     * @return The wrong default columns.
     */
    public List<ErrorTupel> getWrongDefaultColumns() {
        return wrongDefaultColumns;
    }

    /**
     * Sets the wrong default columns.
     *
     * @param wrongDefaultColumns The wrong default columns.
     */
    public void setWrongDefaultColumns(List<ErrorTupel> wrongDefaultColumns) {
        this.wrongDefaultColumns = wrongDefaultColumns;
    }

    /**
     * Add wrong default column.
     *
     * @param column The column.
     */
    public void addWrongDefaultColumn(ErrorTupel column) {
        this.wrongDefaultColumns.add(column);
    }

    /**
     * Gets the wrong null columns.
     *
     * @return The wrong null columns.
     */
    public List<ErrorTupel> getWrongNullColumns() {
        return wrongNullColumns;
    }

    /**
     * Sets the wrong null columns.
     *
     * @param wrongNullColumns The wrong null columns.
     */
    public void setWrongNullColumns(List<ErrorTupel> wrongNullColumns) {
        this.wrongNullColumns = wrongNullColumns;
    }

    /**
     * Add wrong null column.
     *
     * @param column The column.
     */
    public void addWrongNullColumn(ErrorTupel column) {
        this.wrongNullColumns.add(column);
    }
    //endregion
}
