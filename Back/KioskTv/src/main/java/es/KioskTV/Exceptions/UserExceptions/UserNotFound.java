package es.KioskTV.Exceptions.UserExceptions;

/**
 * Exception to indicate that a user record is not found.
 */
public class UserNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UserNotFound with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public UserNotFound(String message) {
        super(message);
    }
}