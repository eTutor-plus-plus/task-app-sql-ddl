package at.jku.dke.task_app.sql_ddl.controllers;

import at.jku.dke.etutor.task_app.controllers.BaseTaskController;
import at.jku.dke.task_app.sql_ddl.data.entities.SqlDdlTask;
import at.jku.dke.task_app.sql_ddl.dto.ModifySqlDdlTaskDto;
import at.jku.dke.task_app.sql_ddl.dto.SqlDdlTaskDto;
import at.jku.dke.task_app.sql_ddl.services.SqlDdlTaskService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing {@link SqlDdlTask}s.
 */
@RestController
public class TaskController extends BaseTaskController<SqlDdlTask, SqlDdlTaskDto, ModifySqlDdlTaskDto> {

    /**
     * Creates a new instance of class {@link TaskController}.
     *
     * @param taskService The task service.
     */
    public TaskController(SqlDdlTaskService taskService) {
        super(taskService);
    }


    @Override
    protected SqlDdlTaskDto mapToDto(SqlDdlTask entity) {
        return new SqlDdlTaskDto(entity.getSolution(), entity.getTablePoints(), entity.getColumnPoints(),
            entity.getPrimaryKeyPoints(), entity.getForeignKeyPoints(), entity.getConstraintPoints(), entity.getInsertStatements());
    }
}
