package at.jku.dke.task_app.sql_ddl.evaluation.analysisObjects;

/**
 * The type Error tupel.
 */
public class ErrorTupel {
    /**
     * The Source.
     */
//region Fields
    String source;
    /**
     * The Error.
     */
    String error;
    //endregion

    /**
     * Creates a new instance of class Error tupel.
     *
     * @param source The source.
     * @param error  The error.
     */
    public ErrorTupel(String source, String error) {
        this.source = source;
        this.error = error;
    }

    //region Getter/Setter

    /**
     * Gets the source.
     *
     * @return The source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source.
     *
     * @param source The source.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the error.
     *
     * @return The error.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error.
     *
     * @param error The error.
     */
    public void setError(String error) {
        this.error = error;
    }

    //endregion
}
