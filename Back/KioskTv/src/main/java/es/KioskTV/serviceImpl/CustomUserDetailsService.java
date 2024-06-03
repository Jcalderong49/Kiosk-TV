package es.KioskTV.serviceImpl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import es.KioskTV.Repository.UserRepository;
import es.KioskTV.entity.User;

/**
 * Implementation of UserDetailsService interface.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String das) throws NoSuchElementException {
		User user = userRepository.findByDas(das).orElseThrow();
		return user;
	}
}