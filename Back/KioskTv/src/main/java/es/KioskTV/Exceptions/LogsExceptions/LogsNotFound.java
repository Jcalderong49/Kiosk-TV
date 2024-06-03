package es.KioskTV.Exceptions.LogsExceptions;

/**
 * Exception to indicate that a log record is not found.
 */
public class LogsNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new LogsNotFound with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public LogsNotFound(String message) {
        super(message);
    }
}