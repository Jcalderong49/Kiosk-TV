package es.KioskTV.entityDTO;

import java.util.Set;

/**
 * Represents the response object containing JWT token and user roles.
 */
public class JwtAuthenticationResponse {
    private String token;
    private Set<String> roles;

    public JwtAuthenticationResponse(String token, Set<String> roles) {
        this.token = token;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    /**
     * Creates a new JwtAuthenticationResponseBuilder instance.
     * 
     * @return JwtAuthenticationResponseBuilder instance.
     */
    public static JwtAuthenticationResponseBuilder builder() {
        return new JwtAuthenticationResponseBuilder();
    }

    /**
     * Builder class for JwtAuthenticationResponse.
     */
    public static class JwtAuthenticationResponseBuilder {
        private String token;
        private Set<String> roles;

        /**
         * Sets the token for the response.
         * 
         * @param token The JWT token.
         * @return JwtAuthenticationResponseBuilder instance.
         */
        public JwtAuthenticationResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * Sets the roles for the response.
         * 
         * @param roles The user roles.
         * @return JwtAuthenticationResponseBuilder instance.
         */
        public JwtAuthenticationResponseBuilder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        /**
         * Builds and returns the JwtAuthenticationResponse instance.
         * 
         * @return JwtAuthenticationResponse instance.
         */
        public JwtAuthenticationResponse build() {
            return new JwtAuthenticationResponse(token, roles);
        }
    }
}