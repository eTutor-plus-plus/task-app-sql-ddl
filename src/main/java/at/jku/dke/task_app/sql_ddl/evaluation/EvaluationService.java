package at.jku.dke.task_app.sql_ddl.evaluation;

import at.jku.dke.etutor.task_app.dto.CriterionDto;
import at.jku.dke.etutor.task_app.dto.GradingDto;
import at.jku.dke.etutor.task_app.dto.SubmissionMode;
import at.jku.dke.etutor.task_app.dto.SubmitSubmissionDto;
import at.jku.dke.task_app.sql_ddl.config.DbConnectionParameters;
import at.jku.dke.task_app.sql_ddl.data.entities.SqlDdlTask;
import at.jku.dke.task_app.sql_ddl.data.repositories.SqlDdlTaskRepository;
import at.jku.dke.task_app.sql_ddl.dto.SqlDdlSubmissionDto;
import at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects.*;
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
import java.util.*;
import java.util.stream.Collectors;

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
        gradingDto = grade(analysis, task, gradingDto);
        gradingDto = report(analysis, task, gradingDto, submission);

        return gradingDto;

    }

    private GradingDto report(HashMap<DDLEvaluationCriterion, DDLCriterionAnalysis> analysis, SqlDdlTask task, GradingDto gradingDto, SubmitSubmissionDto<SqlDdlSubmissionDto> submission) {
        List<CriterionDto> criteria = new ArrayList<>();
        for (Map.Entry<DDLEvaluationCriterion, DDLCriterionAnalysis> entry : analysis.entrySet()) {
            DDLEvaluationCriterion criterion = entry.getKey();
            DDLCriterionAnalysis criterionAnalysis = entry.getValue();


            if (submission.mode().equals(SubmissionMode.RUN)) {
                if (criterion.equals(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.syntax",null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), criterionAnalysis.getAnalysisException() == null ? null : criterionAnalysis.getAnalysisException().getMessage()));
                    break;
                }

                continue;
            }
            if (submission.feedbackLevel().equals(0)) {
                if (gradingDto.maxPoints().equals(gradingDto.points())) {
                    return new GradingDto(gradingDto.maxPoints(), gradingDto.points(), messageSource.getMessage("feedback.correct", null, Locale.of(submission.language())), criteria);
                } else {
                    return new GradingDto(gradingDto.maxPoints(), gradingDto.points(), messageSource.getMessage("feedback.incorrect", null, Locale.of(submission.language())), criteria);
                }
            }
            if (submission.feedbackLevel().equals(1)) {
                if (criterion.equals(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.syntax", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), criterionAnalysis.getAnalysisException() == null ? null : criterionAnalysis.getAnalysisException().getMessage()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_TABLES)) {
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.tables", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), criterionAnalysis.getAnalysisException() == null ? null : criterionAnalysis.getAnalysisException().getMessage()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_COLUMNS)) {
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.columns", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), criterionAnalysis.getAnalysisException() == null ? null : criterionAnalysis.getAnalysisException().getMessage()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS)) {
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.primaryKeys", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), criterionAnalysis.getAnalysisException() == null ? null : criterionAnalysis.getAnalysisException().getMessage()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS)) {
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.foreignKeys", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), criterionAnalysis.getAnalysisException() == null ? null : criterionAnalysis.getAnalysisException().getMessage()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_CONSTRAINTS)) {
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.constraints", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), criterionAnalysis.getAnalysisException() == null ? null : criterionAnalysis.getAnalysisException().getMessage()));
                }
            }
            if (submission.feedbackLevel().equals(2)) {
                if (criterion.equals(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
                    SyntaxAnalysis syntaxAnalysis = (SyntaxAnalysis) criterionAnalysis;
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.syntax", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), syntaxAnalysis.getErrorDescription()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_TABLES)) {
                    TablesAnalysis tablesAnalysis = (TablesAnalysis) criterionAnalysis;
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.tables", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), tablesAnalysis.isCriterionSatisfied() ? null : messageSource.getMessage("criterium.missingTables", null, Locale.of(submission.language()))+": " + tablesAnalysis.getMissingTables().size() + " <br> "+messageSource.getMessage("criterium.surplusTables", null, Locale.of(submission.language()))+": " + tablesAnalysis.getSurplusTables().size()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_COLUMNS)) {
                    ColumnsAnalysis columnsAnalysis = (ColumnsAnalysis) criterionAnalysis;
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.columns", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), columnsAnalysis.isCriterionSatisfied() ? null : messageSource.getMessage("criterium.missingColumns", null, Locale.of(submission.language()))+": " + columnsAnalysis.getColumnsOfTables().size()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS)) {
                    PrimaryKeysAnalysis primaryKeysAnalysis = (PrimaryKeysAnalysis) criterionAnalysis;
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.primaryKeys", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), primaryKeysAnalysis.isCriterionSatisfied() ? null : messageSource.getMessage("criterium.missingPrimaryKeys", null, Locale.of(submission.language()))+": "+ primaryKeysAnalysis.getMissingPrimaryKeys().size() + " <br>"+ messageSource.getMessage("criterium.surplusPrimaryKeys", null, Locale.of(submission.language())) +": " + primaryKeysAnalysis.getSurplusPrimaryKeys().size()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS)) {
                    ForeignKeysAnalysis foreignKeysAnalysis = (ForeignKeysAnalysis) criterionAnalysis;
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.foreignKeys", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), foreignKeysAnalysis.isCriterionSatisfied() ? null : messageSource.getMessage("criterium.missingForeignKeys", null, Locale.of(submission.language()))+": " + foreignKeysAnalysis.getMissingForeignKeys().size() + " <br> "+messageSource.getMessage("criterium.surplusForeignKeys", null, Locale.of(submission.language()))+": " + foreignKeysAnalysis.getSurplusForeignKeys().size() + " <br>"+messageSource.getMessage("criterium.wrongUpdateForeignKeys", null, Locale.of(submission.language()))+": " + foreignKeysAnalysis.getWrongUpdateForeignKeys().size() + messageSource.getMessage("criterium.wrongDeleteForeignKeys", null, Locale.of(submission.language())) + foreignKeysAnalysis.getWrongDeleteForeignKeys().size()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_CONSTRAINTS)) {
                    ConstraintsAnalysis constraintsAnalysis = (ConstraintsAnalysis) criterionAnalysis;
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.constraints", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), constraintsAnalysis.isCriterionSatisfied() ? null : messageSource.getMessage("criterium.missingConstraints", null, Locale.of(submission.language()))+": " + constraintsAnalysis.getMissingConstraints().size() + " <br> "+messageSource.getMessage("criterium.surplusConstraints", null, Locale.of(submission.language()))+": " + constraintsAnalysis.getSurplusConstraints().size()));
                }
            }
            if (submission.feedbackLevel().equals(3)) {
                if (criterion.equals(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
                    assert criterionAnalysis instanceof SyntaxAnalysis;
                    SyntaxAnalysis syntaxAnalysis = (SyntaxAnalysis) criterionAnalysis;
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.syntax", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), syntaxAnalysis.getErrorDescription()));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_TABLES)) {
                    assert criterionAnalysis instanceof TablesAnalysis;
                    TablesAnalysis tablesAnalysis = (TablesAnalysis) criterionAnalysis;
                    String s = "";

                    if(!tablesAnalysis.isMissingTablesEmpty()) {
                         s += messageSource.getMessage("criterium.missingTables", null, Locale.of(submission.language()))+": " + String.join(", ", tablesAnalysis.getMissingTables());
                    }
                    if(!tablesAnalysis.isSurplusTablesEmpty())
                        s += "<br>"+messageSource.getMessage("criterium.surplusTables", null, Locale.of(submission.language()))+": " + String.join(", ", tablesAnalysis.getSurplusTables());

                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.tables", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), tablesAnalysis.isCriterionSatisfied() ? null : s));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_COLUMNS)) {
                    assert criterionAnalysis instanceof ColumnsAnalysis;
                    ColumnsAnalysis columnsAnalysis = (ColumnsAnalysis) criterionAnalysis;
                    String s = "";
                    for (ColumnsOfTable table : columnsAnalysis.getColumnsOfTables()) {
                        if (!table.isMissingColumnsEmpty()) {
                            s += messageSource.getMessage("criterium.missingColumns", null, Locale.of(submission.language()))+": " + String.join(", ", table.getMissingColumns().stream()
                                .map(ErrorTupel::getError)
                                .collect(Collectors.toList()));
                        }
                        if (!table.isSurplusColumnsEmpty()) {
                            s += "<br> "+messageSource.getMessage("criterium.surplusColumns", null, Locale.of(submission.language()))+": " + String.join(", ", table.getSurplusColumns().stream()
                                .map(ErrorTupel::getError)
                                .collect(Collectors.toList()));
                        }
                        if (!table.isWrongDatatypeColumnsEmpty()) {
                            s += "<br> "+messageSource.getMessage("criterium.wrongDatatypeColumns", null, Locale.of(submission.language()))+": " + String.join(", ", table.getWrongDatatypeColumns().stream()
                                .map(ErrorTupel::getError)
                                .collect(Collectors.toList()));
                        }
                        if (!table.isWrongDefaultColumnsEmpty()) {
                            s += "<br> "+messageSource.getMessage("criterium.wrongDefaultColumns", null, Locale.of(submission.language()))+": " + String.join(", ", table.getWrongDefaultColumns().stream()
                                .map(ErrorTupel::getError)
                                .collect(Collectors.toList()));
                        }
                        if (!table.isWrongNullColumnsEmpty()) {
                            s += "<br> "+messageSource.getMessage("criterium.wrongNullColumns", null, Locale.of(submission.language()))+": " + String.join(", ", table.getWrongNullColumns().stream()
                                .map(ErrorTupel::getError)
                                .collect(Collectors.toList()));
                        }
                    }

                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.columns", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), columnsAnalysis.isCriterionSatisfied() ? null : s));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS)) {
                    assert criterionAnalysis instanceof PrimaryKeysAnalysis;
                    PrimaryKeysAnalysis primaryKeysAnalysis = (PrimaryKeysAnalysis) criterionAnalysis;
                    String s = "";
                    if(!primaryKeysAnalysis.isMissingPrimaryKeysEmpty())
                    {
                        s += messageSource.getMessage("criterium.missingPrimaryKeys", null, Locale.of(submission.language()))+": " + String.join(", ", primaryKeysAnalysis.getMissingPrimaryKeys().stream()
                            .map(ErrorTupel::getError)
                            .collect(Collectors.toList()));
                    }
                    if(!primaryKeysAnalysis.isSurplusPrimaryKeysEmpty())
                    {
                        s += "<br> "+messageSource.getMessage("criterium.surplusPrimaryKeys", null, Locale.of(submission.language()))+": " + String.join(", ", primaryKeysAnalysis.getSurplusPrimaryKeys().stream()
                            .map(ErrorTupel::getError)
                            .collect(Collectors.toList()));
                    }

                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.primaryKeys", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), primaryKeysAnalysis.isCriterionSatisfied() ? null : s));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS)) {
                    assert criterionAnalysis instanceof ForeignKeysAnalysis;
                    ForeignKeysAnalysis foreignKeysAnalysis = (ForeignKeysAnalysis) criterionAnalysis;
                    String s = "";
                    if(!foreignKeysAnalysis.isMissingForeignKeysEmpty())
                    {

                        s += messageSource.getMessage("criterium.missingForeignKeys", null, Locale.of(submission.language()))+": " + String.join(", ", foreignKeysAnalysis.getMissingForeignKeys().stream()
                            .map(ErrorTupel::getError)
                            .collect(Collectors.toList()));
                    }
                    if(!foreignKeysAnalysis.isSurplusForeignKeysEmpty())
                    {
                        s += "<br> "+messageSource.getMessage("criterium.surplusForeignKeys", null, Locale.of(submission.language()))+": " + String.join(", ", foreignKeysAnalysis.getSurplusForeignKeys().stream()
                            .map(ErrorTupel::getError)
                            .collect(Collectors.toList()));
                    }
                    if(!foreignKeysAnalysis.isWrongUpdateForeignKeysEmpty())
                    {
                        s += "<br> "+messageSource.getMessage("criterium.wrongUpdateForeignKeys", null, Locale.of(submission.language()))+": " + String.join(", ", foreignKeysAnalysis.getWrongUpdateForeignKeys().stream()
                            .map(ErrorTupel::getError)
                            .collect(Collectors.toList()));
                    }
                    if(!foreignKeysAnalysis.isWrongDeleteForeignKeysEmpty())
                    {
                        s += "<br> "+messageSource.getMessage("criterium.wrongDeleteForeignKeys", null, Locale.of(submission.language()))+": " + String.join(", ", foreignKeysAnalysis.getWrongDeleteForeignKeys().stream()
                            .map(ErrorTupel::getError)
                            .collect(Collectors.toList()));
                    }
                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.foreignKeys", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), foreignKeysAnalysis.isCriterionSatisfied() ? null : s));
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_CONSTRAINTS)) {
                    assert criterionAnalysis instanceof ConstraintsAnalysis;
                    ConstraintsAnalysis constraintsAnalysis = (ConstraintsAnalysis) criterionAnalysis;
                    String s = "";
                    if(!constraintsAnalysis.isMissingConstraintsEmpty())
                    {
                        s += messageSource.getMessage("criterium.missingConstraints", null, Locale.of(submission.language()))+": " + String.join(", ", constraintsAnalysis.getMissingConstraints().stream()
                            .map(ErrorTupel::getError)
                            .collect(Collectors.toList()));
                    }
                    if(!constraintsAnalysis.isSurplusConstraintsEmpty())
                    {
                        s += "<br> "+messageSource.getMessage("criterium.surplusConstraints", null, Locale.of(submission.language()))+": " + String.join(", ", constraintsAnalysis.getSurplusConstraints().stream()
                            .map(ErrorTupel::getError)
                            .collect(Collectors.toList()));
                    }
//                    if(!constraintsAnalysis.isDmlStatementsWithMistakesEmpty())
//                    {
//                        s += "<br> Wrong DML Statements: " + String.join(", ", constraintsAnalysis.getDmlStatementsWithMistakes());
//                    }

                    criteria.add(new CriterionDto(messageSource.getMessage("criterium.constraints", null, Locale.of(submission.language())), null, criterionAnalysis.isCriterionSatisfied(), constraintsAnalysis.isCriterionSatisfied() ? null : s));
                }
            }

        }
        return new GradingDto(gradingDto.maxPoints(), gradingDto.points(), gradingDto.generalFeedback(), criteria);
    }

    public GradingDto grade(HashMap<DDLEvaluationCriterion, DDLCriterionAnalysis> analysis, SqlDdlTask task, GradingDto gradingDto) {

        long points = 0;
        String msg = null;
        boolean correctTables = false;
        boolean correctColumns = false;
        for (Map.Entry<DDLEvaluationCriterion, DDLCriterionAnalysis> entry : analysis.entrySet()) {
            DDLEvaluationCriterion criterion = entry.getKey();
            DDLCriterionAnalysis criterionAnalysis = entry.getValue();


            if (criterion.equals(DDLEvaluationCriterion.CORRECT_TABLES)) {
                TablesAnalysis tablesAnalysis = (TablesAnalysis) criterionAnalysis;
                points += task.getTablePoints() * tablesAnalysis.getTotalNumOfTables()
                    - task.getTablePoints() * tablesAnalysis.getMissingTables().size()
                    - task.getTablePoints() * tablesAnalysis.getSurplusTables().size();

            } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_COLUMNS)) {
                ColumnsAnalysis columnsAnalysis = (ColumnsAnalysis) criterionAnalysis;
                for (ColumnsOfTable table : columnsAnalysis.getColumnsOfTables()) {
                    if (table.isMissingColumnsEmpty() && table.isSurplusColumnsEmpty() && table.isWrongDatatypeColumnsEmpty() && table.isWrongDefaultColumnsEmpty() && table.isWrongNullColumnsEmpty()) {
                        points += task.getColumnPoints();
                    }
                }


            } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS)) {
                PrimaryKeysAnalysis primaryKeysAnalysis = (PrimaryKeysAnalysis) criterionAnalysis;
                points += (long) task.getPrimaryKeyPoints() * primaryKeysAnalysis.getTotalPrimaryKeys()
                    - (long) task.getPrimaryKeyPoints() * primaryKeysAnalysis.getMissingPrimaryKeys().size()
                    - (long) task.getPrimaryKeyPoints() * primaryKeysAnalysis.getSurplusPrimaryKeys().size();
            } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS)) {
                ForeignKeysAnalysis foreignKeysAnalysis = (ForeignKeysAnalysis) criterionAnalysis;
                points += task.getForeignKeyPoints() * foreignKeysAnalysis.getTotalForeignKeys()
                    - task.getForeignKeyPoints() * foreignKeysAnalysis.getMissingForeignKeys().size()
                    - task.getForeignKeyPoints() * foreignKeysAnalysis.getSurplusForeignKeys().size()
                    - task.getForeignKeyPoints() * foreignKeysAnalysis.getWrongUpdateForeignKeys().size()
                    - task.getForeignKeyPoints() * foreignKeysAnalysis.getWrongDeleteForeignKeys().size();
            }


            if (criterionAnalysis.getAnalysisException() == null && criterionAnalysis.isCriterionSatisfied()) {
                // Add the points for this criterion to the total points
                if (criterion.equals(DDLEvaluationCriterion.CORRECT_TABLES)) {
                    correctTables = true;
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_COLUMNS)) {
                    points += task.getColumnPoints();
                    correctColumns = true;
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS)) {
                    points += task.getPrimaryKeyPoints();
                } else if (criterion.equals(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS)) {
                    points += task.getForeignKeyPoints();
                }
            }

        }
        for (Map.Entry<DDLEvaluationCriterion, DDLCriterionAnalysis> entry : analysis.entrySet()) {
            DDLEvaluationCriterion criterion = entry.getKey();
            DDLCriterionAnalysis criterionAnalysis = entry.getValue();


            // Check that there was no exception analysing the criterion and also the criterion is satisfied
            if (criterionAnalysis.getAnalysisException() == null && criterionAnalysis.isCriterionSatisfied()) {
                // Add the points for this criterion to the total points
                if (criterion.equals(DDLEvaluationCriterion.CORRECT_CONSTRAINTS) && correctTables && correctColumns) {
                    points += task.getConstraintPoints();
                }
            } else {
                // Check if the criterion is Syntax -> return with 0 points and log
                if (criterion.equals(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
                    msg = "SQL DDL Syntax not correct.";
                    LOG.info(msg);

                    return new GradingDto(task.getMaxPoints(), BigDecimal.ZERO, msg, null);
                }
            }
        }

        // Set the reached points
        gradingDto = new GradingDto(task.getMaxPoints(), BigDecimal.valueOf(points), null, null);

        return gradingDto;
    }
}
