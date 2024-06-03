package es.KioskTV.Exceptions.LogsExceptions;

/**
 * Exception to indicate that the data for a log record is invalid.
 */
public class InvalidLogsDataException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidLogsDataException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public InvalidLogsDataException(String message) {
        super(message);
    }
}