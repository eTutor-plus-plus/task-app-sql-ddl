package at.jku.dke.task_app.sql_ddl.evaluation;

import at.jku.dke.etutor.task_app.dto.GradingDto;
import at.jku.dke.etutor.task_app.dto.SubmissionMode;
import at.jku.dke.etutor.task_app.dto.SubmitSubmissionDto;
import at.jku.dke.task_app.sql_ddl.config.DbConnectionParameters;
import at.jku.dke.task_app.sql_ddl.data.repositories.SqlDdlTaskRepository;
import at.jku.dke.task_app.sql_ddl.dto.SqlDdlSubmissionDto;
import at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects.DDLCriterionAnalysis;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service that evaluates submissions.
 */
@Service
public class EvaluationService {
    private static final Logger LOG = LoggerFactory.getLogger(EvaluationService.class);

    private final SqlDdlTaskRepository taskRepository;
    private final MessageSource messageSource;
    private static final String LINE_SEP = System.getProperty("line.separator", "\n");

    /**
     * Creates a new instance of class {@link EvaluationService}.
     *
     * @param taskRepository         The task repository.
     * @param messageSource          The message source.
     * @param dbConnectionParameters The db connection parameters.
     */
    public EvaluationService(SqlDdlTaskRepository taskRepository, MessageSource messageSource, DbConnectionParameters dbConnectionParameters) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
        DBHelper.init(dbConnectionParameters);

    }

    /**
     * Evaluates a input.
     *
     * @param submission The input to evaluate.
     * @return The evaluation result.
     */
    @Transactional
    public GradingDto evaluate(SubmitSubmissionDto<SqlDdlSubmissionDto> submission) {
        // find task
        var task = this.taskRepository.findById(submission.taskId()).orElseThrow(() -> new EntityNotFoundException("Task " + submission.taskId() + " does not exist."));
        GradingDto gradingDto = new GradingDto(task.getMaxPoints(), BigDecimal.ZERO, null, null);
        DDLAnalyzerConfig analyzerConfig = new DDLAnalyzerConfig();
        DDLAnalyzer analyzer = new DDLAnalyzer();

        String solutionSchema;
        String tempDMLStatements;
        List<String> dmlStatements;
        String query;
        Statement stmt;
        ResultSet rs;

        String action;

        int diagnoseLevel;
        boolean ok;

        String user;
        String userPwd;
        String userSchema;

        DBUserAdmin admin;
        Connection systemConn;
        Connection exerciseConnection;
        Connection userConn;

        // Get the passed values
        action = submission.mode().toString();
        diagnoseLevel = submission.feedbackLevel();


        // Get the system connection
        try {
            systemConn = DBHelper.getSystemConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Check if the connection is successfully up
        if (systemConn == null)
            return null;

        // Get the schema with the solution for the exercise
        solutionSchema = "";
        tempDMLStatements = "";

        query = "";
        query = query.concat("SELECT	schema_name, insert_statements " + LINE_SEP);
        query = query.concat("FROM 		task " + LINE_SEP);
        query = query.concat("WHERE 	id = " + submission.taskId() + LINE_SEP);

        try {
            stmt = systemConn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                // note: this is the correct query for the exercise
                solutionSchema = rs.getString("schema_name");
                tempDMLStatements = rs.getString("insert_statements");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        // Get the system connection with the schema of the exercise solution
        try {
            exerciseConnection = DBHelper.getSystemConnectionWithSchema(solutionSchema);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Check if the exercise connection is successfully up
        if (exerciseConnection == null)
            return null;

        // Set the exercise connection
        analyzerConfig.setExerciseConn(exerciseConnection);

        // Set the dml statements for the check constraints
        if (tempDMLStatements != null) {
            dmlStatements = List.of(tempDMLStatements.replace("\n", "").split(";"));
            analyzerConfig.setDmlStatements(dmlStatements);
        }

        // Get user connection
        admin = DBUserAdmin.getAdmin();
        user = admin.getUser();
        userPwd = admin.getPwd(user);
        userSchema = admin.getSchema(user);

        userConn = DBHelper.getUserConnection(user, userPwd, userSchema);

        // Check if the user connection is successfully up
        if (userConn == null)
            return null;

        // Set the user connection
        analyzerConfig.setUserConn(userConn);

        // Configure analyzer
        if (submission.mode().equals(SubmissionMode.RUN)) {
            analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_SYNTAX);
            analyzerConfig.setDiagnoseLevel(1);
        } else {
            analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_SYNTAX);
            analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_TABLES);
            analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_COLUMNS);
            analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS);
            analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS);
            analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_CONSTRAINTS);
            analyzerConfig.setDiagnoseLevel(diagnoseLevel);
        }
        HashMap<DDLEvaluationCriterion, DDLCriterionAnalysis> analysis = null;
        // Execute analysis
        try {
            analysis = analyzer.analyze(submission.submission().input(), analyzerConfig);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBHelper.resetUserConnection(userConn, user, userSchema);
        admin.releaseUser(user);
        DBHelper.closeSystemConnectionWithSchema();


        //testoutput
        for (Map.Entry<DDLEvaluationCriterion, DDLCriterionAnalysis> entry : analysis.entrySet()) {
            DDLEvaluationCriterion criterion = entry.getKey();
            DDLCriterionAnalysis criterionAnalysis = entry.getValue();

            System.out.println("Criterion: " + criterion);
            System.out.println("Criterion Analysis: " + criterionAnalysis.isCriterionSatisfied());
            System.out.println("-----------------------------");
        }
        //TODO generate gradingDTO from analysis like: buildReport(analysis, submission)
        return gradingDto;

        // Check for each criterion if the solution is correct
//        criterionAnalysisIterator = analysis.iterCriterionAnalysis();
//        while (criterionAnalysisIterator.hasNext()) {
//            criterionAnalysis = criterionAnalysisIterator.next();
//            if(!criterionAnalysis.isCriterionSatisfied() || criterionAnalysis.getAnalysisException() != null) {
//                ok = false;
//            }
//        }
//
//        // Set if the submission suits the solution
//        analysis.setSubmissionSuitsSolution(ok);
//
//        // Reset user schema and release user
//        DBHelper.resetUserConnection(userConn, user, userSchema);
//        admin.releaseUser(user);
//
//        // Set the exercise id in the analysis object
//        analysis.setExerciseId(exerciseID);
//
//        // Close connection
//        DBHelper.closeSystemConnectionWithSchema();
//
//        return analysis;
//        return null;
    }

}
