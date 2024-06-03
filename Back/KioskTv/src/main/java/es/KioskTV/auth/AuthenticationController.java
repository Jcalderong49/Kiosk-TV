package es.KioskTV.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.KioskTV.entityDTO.JwtAuthenticationResponse;
import es.KioskTV.request.SignUpRequest;
import es.KioskTV.request.SigninRequest;
import es.KioskTV.service.AuthenticationService;

/**
 * This class is a REST controller for handling authentication requests.
 * It contains endpoints for user sign-up and sign-in operations.
 */
@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Constructs an AuthenticationController with the specified
     * AuthenticationService.
     *
     * @param authenticationService the service used for authentication operations
     */
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Handles the sign-up request. Creates a new user account.
     *
     * @param request the sign-up request containing user details
     * @return a ResponseEntity containing a JwtAuthenticationResponse
     */
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        JwtAuthenticationResponse response = authenticationService.signup(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles the sign-in request. Authenticates the user.
     *
     * @param request the sign-in request containing user credentials
     * @return a ResponseEntity containing a JwtAuthenticationResponse
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        JwtAuthenticationResponse response = authenticationService.signin(request);
        return ResponseEntity.ok(response);
    }
}
