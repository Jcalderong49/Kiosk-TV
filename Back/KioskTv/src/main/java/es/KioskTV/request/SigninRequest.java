package es.KioskTV.request;

/**
 * Represents a request for signing in.
 */
public class SigninRequest {

    private String das;
    private String password;

    /**
     * Constructs a new SigninRequest with the specified DAS and password.
     *
     * @param das      the DAS of the user.
     * @param password the password of the user.
     */
    public SigninRequest(String das, String password) {
        this.das = das;
        this.password = password;
    }

    public String getDas() {
        return das;
    }

    public void setDas(String das) {
        this.das = das;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}