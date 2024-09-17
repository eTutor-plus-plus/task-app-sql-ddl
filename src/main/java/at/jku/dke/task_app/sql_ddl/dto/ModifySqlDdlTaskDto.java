package at.jku.dke.task_app.sql_ddl.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link at.jku.dke.task_app.sql_ddl.data.entities.SqlDdlTask}
 */
public record ModifySqlDdlTaskDto(@NotNull String solution, @NotNull Integer tablePoints, @NotNull Integer columnPoints,
                                  @NotNull Integer primaryKeyPoints, @NotNull Integer foreignKeyPoints, @NotNull Integer constraintPoints,
                                  @NotNull String insertStatements) implements Serializable {

}
