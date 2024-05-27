package at.jku.dke.task_app.sql_ddl.controllers;

import at.jku.dke.etutor.task_app.controllers.BaseSubmissionController;
import at.jku.dke.task_app.sql_ddl.data.entities.BinarySearchSubmission;
import at.jku.dke.task_app.sql_ddl.dto.BinarySearchSubmissionDto;
import at.jku.dke.task_app.sql_ddl.services.BinarySearchSubmissionService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing {@link BinarySearchSubmission}s.
 */
@RestController
public class SubmissionController extends BaseSubmissionController<BinarySearchSubmissionDto> {
    /**
     * Creates a new instance of class {@link SubmissionController}.
     *
     * @param submissionService The input service.
     */
    public SubmissionController(BinarySearchSubmissionService submissionService) {
        super(submissionService);
    }
}
