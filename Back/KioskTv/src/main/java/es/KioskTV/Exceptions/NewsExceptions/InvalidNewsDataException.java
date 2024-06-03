package es.KioskTV.Exceptions.NewsExceptions;

/**
 * Exception to indicate that the data for a news record is invalid.
 */
public class InvalidNewsDataException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidNewsDataException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public InvalidNewsDataException(String message) {
        super(message);
    }

}