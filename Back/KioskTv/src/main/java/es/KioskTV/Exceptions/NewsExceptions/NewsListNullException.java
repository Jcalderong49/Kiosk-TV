package es.KioskTV.Exceptions.NewsExceptions;

/**
 * Exception to indicate that the list of news records is null or empty.
 */
public class NewsListNullException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new NewsListNullException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public NewsListNullException(String message) {
        super(message);
    }

}