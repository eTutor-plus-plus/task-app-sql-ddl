package at.jku.dke.task_app.sql_ddl.controllers;

import at.jku.dke.etutor.task_app.controllers.BaseSubmissionController;
import at.jku.dke.task_app.sql_ddl.data.entities.SqlDdlSubmission;
import at.jku.dke.task_app.sql_ddl.dto.SqlDdlSubmissionDto;
import at.jku.dke.task_app.sql_ddl.services.SqlDdlSubmissionService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing {@link SqlDdlSubmission}s.
 */
@RestController
public class SubmissionController extends BaseSubmissionController<SqlDdlSubmissionDto> {
    /**
     * Creates a new instance of class {@link SubmissionController}.
     *
     * @param submissionService The input service.
     */
    public SubmissionController(SqlDdlSubmissionService submissionService) {
        super(submissionService);
    }
}
