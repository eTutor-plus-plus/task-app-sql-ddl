package at.jku.dke.task_app.sql_ddl.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link at.jku.dke.task_app.sql_ddl.data.entities.SqlDdlSubmission}
 */
public record SqlDdlSubmissionDto(@NotNull String input) implements Serializable {

}
