package es.KioskTV.Exceptions.NewsExceptions;

/**
 * Exception to indicate that a news record is not found.
 */
public class NewsNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new NewsNotFound with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public NewsNotFound(String message) {
        super(message);
    }
}