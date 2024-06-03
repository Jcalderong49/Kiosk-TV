package es.KioskTV.Exceptions.UserExceptions;

/**
 * exception to indicate that the user with a specific DAS is not found
 */
public class DasNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DasNotFoundException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public DasNotFoundException(String message) {
        super(message);
    }
}