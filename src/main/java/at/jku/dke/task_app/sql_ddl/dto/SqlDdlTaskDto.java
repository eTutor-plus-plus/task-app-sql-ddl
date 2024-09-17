package at.jku.dke.task_app.sql_ddl.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link at.jku.dke.task_app.sql_ddl.data.entities.SqlDdlTask}
 */
public record SqlDdlTaskDto(@NotNull String solution,
                            @NotNull Integer tablePoints, @NotNull Integer columnPoints, @NotNull Integer primaryKeyPoints, @NotNull Integer foreignKeyPoints,
                            @NotNull Integer constraintPoints, String insertStatements) implements Serializable {
}
