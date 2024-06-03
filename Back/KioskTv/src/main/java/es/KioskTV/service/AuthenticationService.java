package es.KioskTV.service;

import es.KioskTV.entityDTO.JwtAuthenticationResponse;
import es.KioskTV.request.SignUpRequest;
import es.KioskTV.request.SigninRequest;

/**
 * Service interface for authentication operations.
 */
public interface AuthenticationService {
    /**
     * Registers a new user.
     * 
     * @param request The SignUpRequest object containing user details.
     * @return The JwtAuthenticationResponse object containing JWT token and user
     *         roles.
     */
    JwtAuthenticationResponse signup(SignUpRequest request);

    /**
     * Authenticates a user and generates JWT token.
     * 
     * @param request The SigninRequest object containing user credentials.
     * @return The JwtAuthenticationResponse object containing JWT token and user
     *         roles.
     */
    JwtAuthenticationResponse signin(SigninRequest request);
}