package es.KioskTV.Exceptions.UserExceptions;

/**
 * Exception to indicate that a duplicate user record already exists in the
 * system.
 */
public class DuplicateUserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DuplicateUserException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public DuplicateUserException(String message) {
        super(message);
    }
}