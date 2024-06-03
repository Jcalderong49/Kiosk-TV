package es.KioskTV.Exceptions.UserExceptions;

/**
 * Exception to indicate that the data for a user record is invalid.
 */
public class InvalidUserDataException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidUserDataException with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the
     *                getMessage() method).
     */
    public InvalidUserDataException(String message) {
        super(message);
    }

}