package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;

/**
 * The type Analysis exception.
 */
public class AnalysisException extends Exception {

    /**
     * Creates a new instance of class Analysis exception.
     */
    public AnalysisException() {
        super();
    }

    /**
     * Creates a new instance of class Analysis exception.
     *
     * @param message The message.
     */
    public AnalysisException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of class Analysis exception.
     *
     * @param message The message.
     * @param cause   The cause.
     */
    public AnalysisException(String message, Throwable cause) {
        super(message, cause);
    }

}
