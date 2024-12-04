package at.jku.dke.task_app.sql_ddl.dto;

import at.jku.dke.task_app.sql_ddl.data.entities.Submission;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link Submission}
 */
public record SqlDdlSubmissionDto(@NotNull String input) implements Serializable {

}
