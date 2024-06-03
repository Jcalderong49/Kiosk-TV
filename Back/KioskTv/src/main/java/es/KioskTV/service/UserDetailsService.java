package es.KioskTV.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Service interface for user details operations.
 */
public interface UserDetailsService {
	/**
	 * Loads user details by username.
	 * 
	 * @param das The username (DAS) of the user.
	 * @return The UserDetails object representing the user details.
	 */
	UserDetails loadUserByUsername(String das);
}