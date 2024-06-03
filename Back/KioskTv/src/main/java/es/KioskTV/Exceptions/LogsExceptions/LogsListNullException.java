package es.KioskTV.Exceptions.LogsExceptions;

/**
 * Exception to indicate that the list of log records is null or empty.
 */
public class LogsListNullException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new LogsListNullException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public LogsListNullException(String message) {
        super(message);
    }
}