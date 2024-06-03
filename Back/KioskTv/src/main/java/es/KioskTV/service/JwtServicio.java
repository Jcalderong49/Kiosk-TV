package es.KioskTV.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service interface for JWT operations.
 */
@Service
public interface JwtServicio {
    /**
     * Extracts the username from the JWT token.
     * 
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    String extractUserName(String token);

    /**
     * Generates a JWT token for the given UserDetails.
     * 
     * @param userDetails The UserDetails object.
     * @return The generated JWT token.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Validates if the given JWT token is valid for the given UserDetails.
     * 
     * @param token       The JWT token to validate.
     * @param userDetails The UserDetails object.
     * @return true if the token is valid, false otherwise.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}