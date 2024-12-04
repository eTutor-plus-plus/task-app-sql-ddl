package at.jku.dke.task_app.sql_ddl.services;

import at.jku.dke.etutor.task_app.dto.GradingDto;
import at.jku.dke.etutor.task_app.dto.SubmitSubmissionDto;
import at.jku.dke.etutor.task_app.services.BaseSubmissionService;
import at.jku.dke.task_app.sql_ddl.data.entities.Submission;
import at.jku.dke.task_app.sql_ddl.data.entities.SqlDdlTask;
import at.jku.dke.task_app.sql_ddl.data.repositories.SqlDdlSubmissionRepository;
import at.jku.dke.task_app.sql_ddl.data.repositories.SqlDdlTaskRepository;
import at.jku.dke.task_app.sql_ddl.dto.SqlDdlSubmissionDto;
import at.jku.dke.task_app.sql_ddl.evaluation.EvaluationService;
import org.springframework.stereotype.Service;

/**
 * This class provides methods for managing {@link Submission}s.
 */
@Service
public class SqlDdlSubmissionService extends BaseSubmissionService<SqlDdlTask, Submission, SqlDdlSubmissionDto> {

    private final EvaluationService evaluationService;

    /**
     * Creates a new instance of class {@link SqlDdlSubmissionService}.
     *
     * @param submissionRepository The input repository.
     * @param taskRepository       The task repository.
     * @param evaluationService    The evaluation service.
     */
    public SqlDdlSubmissionService(SqlDdlSubmissionRepository submissionRepository, SqlDdlTaskRepository taskRepository, EvaluationService evaluationService) {
        super(submissionRepository, taskRepository);
        this.evaluationService = evaluationService;
    }

    @Override
    protected Submission createSubmissionEntity(SubmitSubmissionDto<SqlDdlSubmissionDto> submitSubmissionDto) {
        return new Submission(submitSubmissionDto.submission().input());
    }

    @Override
    protected GradingDto evaluate(SubmitSubmissionDto<SqlDdlSubmissionDto> submitSubmissionDto) {
        return this.evaluationService.evaluate(submitSubmissionDto);
    }

    @Override
    protected SqlDdlSubmissionDto mapSubmissionToSubmissionData(Submission submission) {
        return new SqlDdlSubmissionDto(submission.getSubmission());
    }

}
