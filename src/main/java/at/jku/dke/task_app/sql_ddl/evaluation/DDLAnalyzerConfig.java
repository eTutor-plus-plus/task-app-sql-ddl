package at.jku.dke.task_app.sql_ddl.evaluation;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * The type Ddl analyzer config.
 */
public class DDLAnalyzerConfig {
    //region Fields
    private int diagnoseLevel;
    private List<String> dmlStatements;
    private Connection exerciseConn;
    private Connection userConn;
    private HashSet<DDLEvaluationCriterion> evaluationCriteria;
    //endregion

    /**
     * Creates a new instance of class Ddl analyzer config.
     */
    public DDLAnalyzerConfig() {
        this.diagnoseLevel = 0;
        this.dmlStatements = new ArrayList<>();
        this.exerciseConn = null;
        this.userConn = null;
        this.evaluationCriteria = new HashSet<>();
    }

    /**
     * Is criterion to analyze boolean.
     *
     * @param criterion The criterion.
     * @return The boolean.
     */
    public boolean isCriterionToAnalyze(DDLEvaluationCriterion criterion) {
        return this.evaluationCriteria.contains(criterion);
    }

    /**
     * Iter criteria to analyze iterator.
     *
     * @return The iterator.
     */
    public Iterator<DDLEvaluationCriterion> iterCriteriaToAnalyze() {
        return this.evaluationCriteria.iterator();
    }

    /**
     * Add criterion to analyze.
     *
     * @param criterion The criterion.
     */
    public void addCriterionToAnalyze(DDLEvaluationCriterion criterion) {
        this.evaluationCriteria.add(criterion);
    }

    /**
     * Gets the diagnose level.
     *
     * @return The diagnose level.
     */
//region Getter/Setter
    public int getDiagnoseLevel() {
        return diagnoseLevel;
    }

    /**
     * Sets the diagnose level.
     *
     * @param diagnoseLevel The diagnose level.
     */
    public void setDiagnoseLevel(int diagnoseLevel) {
        this.diagnoseLevel = diagnoseLevel;
    }

    /**
     * Gets the dml statements.
     *
     * @return The dml statements.
     */
    public List<String> getDmlStatements() {
        return dmlStatements;
    }

    /**
     * Sets the dml statements.
     *
     * @param dmlStatements The dml statements.
     */
    public void setDmlStatements(List<String> dmlStatements) {
        this.dmlStatements = dmlStatements;
    }

    /**
     * Gets the exercise conn.
     *
     * @return The exercise conn.
     */
    public Connection getExerciseConn() {
        return exerciseConn;
    }

    /**
     * Sets the exercise conn.
     *
     * @param exerciseConn The exercise conn.
     */
    public void setExerciseConn(Connection exerciseConn) {
        this.exerciseConn = exerciseConn;
    }

    /**
     * Gets the user conn.
     *
     * @return The user conn.
     */
    public Connection getUserConn() {
        return userConn;
    }

    /**
     * Sets the user conn.
     *
     * @param userConn The user conn.
     */
    public void setUserConn(Connection userConn) {
        this.userConn = userConn;
    }
    //endregion
}
