package at.jku.dke.task_app.sql_ddl.data.entities;

import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import at.jku.dke.etutor.task_app.dto.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "task")
public class SqlDdlTask extends BaseTask {



    @NotNull
    @Column(name = "solution", nullable = false, length = Integer.MAX_VALUE)
    private String solution;

    @Size(max = 50)
    @Column(name = "schema_name", nullable = false, length = 50)
    private String schemaName;

    @NotNull
    @Column(name = "table_points", nullable = false)
    private Integer tablePoints;

    @NotNull
    @Column(name = "column_points", nullable = false)
    private Integer columnPoints;

    @NotNull
    @Column(name = "primarykey_points", nullable = false)
    private Integer primarykeyPoints;

    @NotNull
    @Column(name = "foreignkey_points", nullable = false)
    private Integer foreignkeyPoints;
    @NotNull
    @Column(name = "constraint_points", nullable = false)
    private Integer constraintPoints;

    @Column(name = "insert_statements", length = Integer.MAX_VALUE)
    private String insertStatements;

    public String getInsertStatements() {
        return insertStatements;
    }

    public void setInsertStatements(String insertStatements) {
        this.insertStatements = insertStatements;
    }

    public SqlDdlTask(Long id, BigDecimal maxPoints, TaskStatus status,String schemaName, String solution,String insertStatements, Integer tablePoints, Integer columnPoints, Integer primarykeyPoints, Integer foreignkeyPoints, Integer constraintPoints) {
        super(id, maxPoints, status);
        this.schemaName = schemaName;
        this.solution = solution;
        this.tablePoints = tablePoints;
        this.columnPoints = columnPoints;
        this.primarykeyPoints = primarykeyPoints;
        this.foreignkeyPoints = foreignkeyPoints;
        this.constraintPoints = constraintPoints;
        this.insertStatements = insertStatements;
    }

    public SqlDdlTask() {

    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public Integer getTablePoints() {
        return tablePoints;
    }

    public void setTablePoints(Integer tablePoints) {
        this.tablePoints = tablePoints;
    }

    public Integer getColumnPoints() {
        return columnPoints;
    }

    public void setColumnPoints(Integer columnPoints) {
        this.columnPoints = columnPoints;
    }

    public Integer getPrimaryKeyPoints() {
        return primarykeyPoints;
    }

    public void setPrimaryKeyPoints(Integer primarykeyPoints) {
        this.primarykeyPoints = primarykeyPoints;
    }

    public Integer getForeignKeyPoints() {
        return foreignkeyPoints;
    }

    public void setForeignKeyPoints(Integer foreignkeyPoints) {
        this.foreignkeyPoints = foreignkeyPoints;
    }

    public Integer getConstraintPoints() {
        return constraintPoints;
    }

    public void setConstraintPoints(Integer constraintPoints) {
        this.constraintPoints = constraintPoints;
    }


/*
 TODO [JPA Buddy] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status", columnDefinition = "task_status(0, 0) not null")
    private Object status;
*/
}
