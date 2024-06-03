package es.KioskTV.Exceptions.UserExceptions;

/**
 * Exception to indicate that the list of users records is null or empty.
 */
public class UserListNullException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UserListNullException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public UserListNullException(String message) {
        super(message);
    }
}