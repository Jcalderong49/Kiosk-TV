package es.KioskTV.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.KioskTV.entity.Role;
import es.KioskTV.Exceptions.UserExceptions.DuplicateUserException;
import es.KioskTV.Exceptions.UserExceptions.UserListNullException;
import es.KioskTV.Exceptions.UserExceptions.UserNotFound;
import es.KioskTV.Repository.UserRepository;
import es.KioskTV.entity.User;
import es.KioskTV.entityDTO.NewDTO;
import es.KioskTV.entityDTO.UserDTO;
import es.KioskTV.entityDTO.UserUpdateDTO;
import es.KioskTV.request.SignUpRequest;
import es.KioskTV.service.JwtServicio;
import es.KioskTV.serviceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Controller for handling user-related operations.
 */
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtServicio jwtService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves a list of all users in the system.
     *
     * @return List of UserDTO representing all users.
     * @throws UserListNullException If the user list is null or empty.
     */
    @Operation(summary = "Get all users", description = "This endpoint returns a list of all users stored in the system.")
    @GetMapping("/api/users/") // Without this bar this endpoint don´t work
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = userService.getAllUser();
        if (users == null || users.isEmpty()) {
            throw new UserListNullException("User list is null or empty.");
        }
        return users;
    }

    /**
     * Retrieves a list of news items associated with a specific user.
     *
     * @param das            The DAS of the user.
     * @param authentication The authentication object representing the current
     *                       user.
     * @return List of NewDTO representing news items associated with the user.
     */
    @Operation(summary = "Get all news by Das user", description = "This endpoint returns a list of all news by Das user.")
    @GetMapping("/api/users/news/{das}")
    public List<NewDTO> getAllNewsByDasUser(@PathVariable String das, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROL_ADMIN.toString()))) {
            Optional<User> searchUser = userRepository.findByDas(das);
            if (!searchUser.isPresent()) {
                throw new UserNotFound("User not found with DAS: " + das);
            } else {
                User findDas = searchUser.get();
                if (!userDetails.getUsername().equals(findDas.getDas())) {
                    throw new UserNotFound("Access denied. User can only access their own news.");
                } else {
                    return userService.getAllNewsByDasUser(das);
                }
            }
        }

        List<NewDTO> newsList = userService.getAllNewsByDasUser(das);
        if (newsList == null || newsList.isEmpty()) {
            throw new UserListNullException("User list is null or empty.");
        }
        return newsList;
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id             The ID of the user.
     * @param authentication The authentication object representing the current
     *                       user.
     * @return UserDTO representing the user.
     */
    @Operation(summary = "Get a user by its ID", description = "This endpoint returns a specific user based on its ID.")
    @GetMapping("/api/users/{id}")
    public UserDTO getUserById(@PathVariable Long id, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROL_ADMIN.toString()))) {
            Optional<User> searchUser = userRepository.findById(id);
            if (!searchUser.isPresent()) {
                throw new UserNotFound("User not found with ID: " + id);
            } else {
                User findDas = searchUser.get();
                if (!userDetails.getUsername().equals(findDas.getDas())) {
                    throw new UserNotFound("Access denied. User can only access their own profile.");
                } else {
                    return userService.getUserById(id).get();
                }
            }
        }

        Optional<User> searchUser = userRepository.findById(id);
        if (!searchUser.isPresent()) {
            throw new UserNotFound("User not found with ID: " + id);
        }
        return userService.getUserById(id).get();
    }

    /**
     * Retrieves a user by its DAS.
     *
     * @param das            The DAS of the user.
     * @param authentication The authentication object representing the current
     *                       user.
     * @return UserDTO representing the user.
     */
    @Operation(summary = "Get a user by its DAS", description = "This endpoint returns a specific user based on its DAS.")
    @GetMapping("/api/usersdas/{das}")
    public UserDTO getUserByDAS(@PathVariable String das, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROL_ADMIN.toString()))) {
            Optional<User> searchUser = userRepository.findByDas(das);
            if (!searchUser.isPresent()) {
                throw new UserNotFound("User not found with ID: " + das);
            } else {
                User findDas = searchUser.get();
                if (!userDetails.getUsername().equals(findDas.getDas())) {
                    throw new UserNotFound("Access denied. User can only access their own profile.");
                } else {
                    return userService.getUserByDas(das).get();
                }
            }
        }

        Optional<User> searchUser = userRepository.findByDas(das);
        if (!searchUser.isPresent()) {
            throw new UserNotFound("User not found with ID: " + das);
        }
        return userService.getUserByDas(das).get();
    }

    /**
     * Retrieves the username from a JWT token.
     *
     * @param token The JWT token.
     * @return ResponseEntity containing the username or an error if not found.
     */
    @Operation(summary = "Get username from JWT token", description = "This endpoint retrieves the username from a provided JWT token.")
    @GetMapping("/api/users/token/{token}")
    public ResponseEntity<String> getUsernameFromToken(@PathVariable String token) {
        String username = null;

        if (token != null) {
            username = jwtService.extractUserName(token);
        }
        if (username != null) {
            return ResponseEntity.ok(username);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new user.
     * 
     * @param userCreateDTO The SignUpRequest object containing user details.
     * @return The created UserDTO object.
     * @throws DuplicateUserException if a user with the provided DAS already
     *                                exists.
     */
    @Operation(summary = "Create a new user", description = "This endpoint creates a new user in the system.")
    @PostMapping("/api/users/")
    public UserDTO createUser(@RequestBody SignUpRequest userCreateDTO) {
        String dasBuscar = userCreateDTO.getDas();
        Optional<User> searchUser = userRepository.findByDas(dasBuscar);
        if (searchUser.isPresent()) {
            throw new DuplicateUserException("User with DAS " + userCreateDTO.getDas() + " already exists.");
        }
        return userService.createUser(userCreateDTO);
    }

    /**
     * Update a user.
     * 
     * @param id             The ID of the user to update.
     * @param userUpdateDTO  The UserUpdateDTO object containing updated user
     *                       details.
     * @param authentication The authentication object.
     * @return The updated UserDTO object.
     * @throws UserNotFound if the user with the given ID is not found or if the
     *                      authenticated user does not have appropriate
     *                      permissions.
     */
    @Operation(summary = "Update a user", description = "This endpoint updates an existing user in the system.")
    @PutMapping("/api/users/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROL_ADMIN.toString()))) {
            Optional<User> searchUser = userRepository.findById(id);
            if (!searchUser.isPresent()) {
                throw new UserNotFound("User not found with ID: " + id);
            } else {
                User findDas = searchUser.get();
                if (!userDetails.getUsername().equals(findDas.getDas())) {
                    throw new UserNotFound("Access denied. User can only access their own profile.");
                } else {
                    return userService.updateUser(id, userUpdateDTO);
                }
            }
        }
        Optional<User> searchUser = userRepository.findById(id);
        if (!searchUser.isPresent()) {
            throw new UserNotFound("User not found with ID: " + id);
        }
        return userService.updateUser(id, userUpdateDTO);
    }

    /**
     * Delete a user.
     * 
     * @param id The ID of the user to delete.
     * @throws UserNotFound if the user with the given ID is not found.
     */
    @Operation(summary = "Delete a user", description = "This endpoint delete an existing user in the system.")
    @PutMapping("/api/users/delete/{id}")
    public void deleteUser(@PathVariable Long id) {

        Optional<User> searchUser = userRepository.findById(id);
        if (!searchUser.isPresent()) {
            throw new UserNotFound("User not found with ID: " + id);
        }
        userService.deactivateUser(id);
    }

    /**
     * Activate a user.
     * 
     * @param id The ID of the user to activate.
     * @throws UserNotFound if the user with the given ID is not found.
     */
    @Operation(summary = "Activate a user", description = "This endpoint activates a previously deactivated user in the system.")
    @PutMapping("/api/users/activate/{id}")
    public void activateUser(@PathVariable Long id) {

        Optional<User> searchUser = userRepository.findById(id);
        if (!searchUser.isPresent()) {
            throw new UserNotFound("User not found with ID: " + id);
        }
        userService.activateUser(id);
    }
}