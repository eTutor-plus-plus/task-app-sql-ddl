package at.jku.dke.task_app.sql_ddl.services;

import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.services.BaseTaskService;
import at.jku.dke.task_app.sql_ddl.data.entities.SqlDdlTask;
import at.jku.dke.task_app.sql_ddl.data.repositories.SqlDdlTaskRepository;
import at.jku.dke.task_app.sql_ddl.dto.ModifySqlDdlTaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * This class provides methods for managing {@link BinarySearchTask}s.
 */
@Service
public class SqlDdlTaskService extends BaseTaskService<SqlDdlTask, ModifySqlDdlTaskDto> {

    private static final Logger LOG = LoggerFactory.getLogger(SqlDdlTaskService.class);

    private final MessageSource messageSource;
    private final String username;
    private final String pwd;
    private final String url;
    private final String schemaName = "schema_name_";

    /**
     * Creates a new instance of class {@link SqlDdlTaskService}.
     *
     * @param repository    The task repository.
     * @param messageSource The message source.
     */
    public SqlDdlTaskService(@Value("${spring.datasource.username}") String username, @Value("${spring.datasource.password}") String pwd, @Value("${spring.datasource.url}") String url, SqlDdlTaskRepository repository, MessageSource messageSource) {
        super(repository);
        this.messageSource = messageSource;

        this.username = username;
        this.pwd = pwd;
        this.url = url;
    }

    @Override
    protected SqlDdlTask createTask(long id, ModifyTaskDto<ModifySqlDdlTaskDto> dto) {
        if (!dto.taskType().equals("sql-ddl")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task type.");
        }
        return new SqlDdlTask(id, dto.maxPoints(), dto.status(), schemaName + id, dto.additionalData().solution(), dto.additionalData().insertStatements(), dto.additionalData().tablePoints(), dto.additionalData().columnPoints(), dto.additionalData().primaryKeyPoints(), dto.additionalData().foreignKeyPoints(), dto.additionalData().constraintPoints());
    }

    @Override
    protected void afterCreate(SqlDdlTask task, ModifyTaskDto<ModifySqlDdlTaskDto> dto) {
        //create schema
        try (Connection con = DriverManager.getConnection(this.url, this.username, this.pwd)) {
            con.setAutoCommit(false);
            String query = "CREATE SCHEMA IF NOT EXISTS " + task.getSchemaName() + " AUTHORIZATION " + username;
            PreparedStatement createSchemaStmt = con.prepareStatement(query);
            LOG.debug("Statement for creating exercise schema: {} ", createSchemaStmt);
            createSchemaStmt.execute();

            // Set schema to exercise schema
            Statement switchSchemaStmt = con.createStatement();
            switchSchemaStmt.execute("SET search_path TO " + task.getSchemaName());

            // Execute ddl solution
            PreparedStatement solutionStmt = con.prepareStatement(task.getSolution());
            solutionStmt.execute();

            // Set schema to public schema
            switchSchemaStmt = con.createStatement();
            switchSchemaStmt.execute("SET search_path TO public");
            con.commit();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating schema: " + e.getMessage());
        }

    }

    @Override
    protected void updateTask(SqlDdlTask task, ModifyTaskDto<ModifySqlDdlTaskDto> dto) {
        if (!dto.taskType().equals("sql-ddl")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task type.");
        }
        task.setSolution(dto.additionalData().solution());
        task.setInsertStatements(dto.additionalData().insertStatements());
        task.setTablePoints(dto.additionalData().tablePoints());
        task.setColumnPoints(dto.additionalData().columnPoints());
        task.setPrimaryKeyPoints(dto.additionalData().primaryKeyPoints());
        task.setForeignKeyPoints(dto.additionalData().foreignKeyPoints());
        task.setConstraintPoints(dto.additionalData().constraintPoints());
    }

    @Override
    protected void afterUpdate(SqlDdlTask task, ModifyTaskDto<ModifySqlDdlTaskDto> dto) {
        try (Connection con = DriverManager.getConnection(this.url, this.username, this.pwd)) {
            con.setAutoCommit(false);

            //delete schema
            LOG.info("Deleting schema: {}", task.getSchemaName());
            String query = "DROP SCHEMA IF EXISTS " + task.getSchemaName() + " CASCADE";
            PreparedStatement dropStmt = con.prepareStatement(query);
            dropStmt.execute();

            //create schema
            query = "CREATE SCHEMA IF NOT EXISTS " + task.getSchemaName() + " AUTHORIZATION " + this.username;
            PreparedStatement createSchemaStmt = con.prepareStatement(query);
            createSchemaStmt.execute();

            // Set schema to exercise schema
            Statement switchSchemaStmt = con.createStatement();
            switchSchemaStmt.execute("SET search_path TO " + task.getSchemaName());

            // Execute ddl solution
            PreparedStatement solutionStmt = con.prepareStatement(task.getSolution());
            solutionStmt.execute();

            // Set schema to public schema
            switchSchemaStmt = con.createStatement();
            switchSchemaStmt.execute("SET search_path TO public");

            con.commit();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating schema: " + e.getMessage());
        }
    }

    @Override
    public void beforeDelete(long id) {

        //delete schema
        try (Connection con = DriverManager.getConnection(this.url, this.username, this.pwd)) {
            con.setAutoCommit(false);

            //delete schema
            LOG.info("Deleting schema: {}", schemaName + id);
            String query = "DROP SCHEMA IF EXISTS " + schemaName + id + " CASCADE";
            PreparedStatement dropStmt = con.prepareStatement(query);
            dropStmt.execute();

            con.commit();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating schema: " + e.getMessage());
        }
    }

}
