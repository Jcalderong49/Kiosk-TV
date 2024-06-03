package es.KioskTV.service;

import java.util.List;
import java.util.Optional;

import es.KioskTV.entityDTO.NewDTO;
import es.KioskTV.entityDTO.UserDTO;
import es.KioskTV.entityDTO.UserUpdateDTO;
import es.KioskTV.request.SignUpRequest;

/**
 * Service interface for user operations.
 */
public interface UserService {
    /**
     * Creates a new user.
     * 
     * @param signUpRequest The SignUpRequest object containing user details.
     * @return The created UserDTO object.
     */
    UserDTO createUser(SignUpRequest signUpRequest);

    /**
     * Retrieves a user by its ID.
     * 
     * @param idUser The ID of the user to retrieve.
     * @return An Optional containing the UserDTO object if found, empty otherwise.
     */
    Optional<UserDTO> getUserById(Long idUser);

    /**
     * Retrieves a user by its DAS (Digital Account System).
     * 
     * @param das The DAS of the user to retrieve.
     * @return An Optional containing the UserDTO object if found, empty otherwise.
     */
    Optional<UserDTO> getUserByDas(String das);

    /**
     * Retrieves all users.
     * 
     * @return A list of UserDTO objects representing all users.
     */
    List<UserDTO> getAllUser();

    /**
     * Updates a user by its ID.
     * 
     * @param idUser        The ID of the user to update.
     * @param userUpdateDTO The UserUpdateDTO object containing updated user
     *                      details.
     * @return The updated UserDTO object.
     */
    UserDTO updateUser(Long idUser, UserUpdateDTO userUpdateDTO);

    /**
     * Activates a user by its ID.
     * 
     * @param idUser The ID of the user to activate.
     */
    void activateUser(Long idUser);

    /**
     * Deactivates a user by its ID.
     * 
     * @param idUser The ID of the user to deactivate.
     */
    void deactivateUser(Long idUser);

    /**
     * Retrieves the user details service.
     * 
     * @return The user details service.
     */
    org.springframework.security.core.userdetails.UserDetailsService userDetailsService();

    /**
     * Retrieves all news items associated with a user by their DAS.
     * 
     * @param das The DAS of the user.
     * @return A list of NewDTO objects representing the news items associated with
     *         the user.
     */
    List<NewDTO> getAllNewsByDasUser(String das);
}