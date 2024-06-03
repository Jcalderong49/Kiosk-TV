package es.KioskTV.Exceptions.NewsExceptions;

/**
 * Exception to indicate that a duplicate news record already exists in the
 * system.
 */
public class DuplicateNewsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DuplicateNewsException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public DuplicateNewsException(String message) {
        super(message);
    }
}