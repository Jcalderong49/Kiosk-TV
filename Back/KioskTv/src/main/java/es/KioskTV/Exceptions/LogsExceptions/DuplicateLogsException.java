package es.KioskTV.Exceptions.LogsExceptions;

/**
 * Exception to indicate that a duplicate log record already exists in the
 * system.
 */
public class DuplicateLogsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DuplicateLogsException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public DuplicateLogsException(String message) {
        super(message);
    }
}