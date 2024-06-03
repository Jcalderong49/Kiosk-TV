package es.KioskTV.serviceImpl;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.KioskTV.Repository.UserRepository;
import es.KioskTV.entity.User;
import es.KioskTV.entityDTO.JwtAuthenticationResponse;
import es.KioskTV.request.SignUpRequest;
import es.KioskTV.request.SigninRequest;
import es.KioskTV.service.AuthenticationService;
import es.KioskTV.service.JwtServicio;

/**
 * Implementation of AuthenticationService interface.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServicio jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtServicio jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if (userRepository.findByDas(request.getDas()).isPresent()) {
            throw new IllegalArgumentException("El das ya existe.");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDas(request.getDas());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);

        // Don´t create the token during the register
        return new JwtAuthenticationResponse(null, Collections.singleton(user.getRole().name()));
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getDas(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByDas(request.getDas())
                .orElseThrow(() -> new IllegalArgumentException("Invalid das or password."));

        String jwt = jwtService.generateToken(user);
        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .roles(roles)
                .build();
    }
}