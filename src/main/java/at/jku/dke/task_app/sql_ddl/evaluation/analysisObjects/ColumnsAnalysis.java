package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;


import at.jku.dke.task_app.sql_ddl.evaluation.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Columns analysis.
 */
public class ColumnsAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private List<ColumnsOfTable> columnsOfTables;
    //endregion

    /**
     * Creates a new instance of class Columns analysis.
     */
    public ColumnsAnalysis() {
        this.columnsOfTables = new ArrayList<>();
    }

    public List<ColumnsOfTable> getColumnsOfTables() {
        return columnsOfTables;
    }

    public void setColumnsOfTables(List<ColumnsOfTable> columnsOfTables) {
        this.columnsOfTables = columnsOfTables;
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }


    public void addColumnsOfTable(ColumnsOfTable columnsOfTable) {
        this.columnsOfTables.add(columnsOfTable);
    }
}
