package es.KioskTV.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import es.KioskTV.Mapper.Mapper;
import es.KioskTV.Repository.NewsRepository;
import es.KioskTV.Repository.UserRepository;
import es.KioskTV.entity.News;
import es.KioskTV.entity.User;
import es.KioskTV.entityDTO.NewDTO;
import es.KioskTV.entityDTO.UserDTO;
import es.KioskTV.entityDTO.UserUpdateDTO;
import es.KioskTV.request.SignUpRequest;
import es.KioskTV.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of the UserService interface
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final NewsRepository newsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Mapper mapper;

    /**
     * user creation method
     *
     * @param userDTO DTO of the user to create
     * @return The DTO of the created user
     */
    @Override
    public UserDTO createUser(SignUpRequest signUpRequest) {
        User newUser = new User();

        newUser.setDas(signUpRequest.getDas());
        newUser.setPassword(signUpRequest.getPassword());
        newUser.setFirstName(signUpRequest.getFirstName());
        newUser.setLastName(signUpRequest.getLastName());
        newUser.setRole(signUpRequest.getRole());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(null);
        newUser.setDeletedAt(null);

        userRepository.save(newUser);

        return mapper.mapUserToDTO(newUser);
    }

    /**
     * Method that obtains a user by its ID
     * 
     * @param idUser User ID of the user to search
     * @return DTO of the user with specific ID
     */
    @Override
    public Optional<UserDTO> getUserById(Long idUser) {
        Optional<User> userOptional = userRepository.findById(idUser);

        User user = userOptional.get();
        return Optional.of(mapper.mapUserToDTO(user));
    }

    /**
     * Method that obtains a user by its das
     * 
     * @param das das of the user to search
     * @return DTO of the user with specific das
     */
    @Override
    public Optional<UserDTO> getUserByDas(String das) {
        Optional<User> userOptional = userRepository.findByDas(das);

        User user = userOptional.get();
        return Optional.of(mapper.mapUserToDTO(user));

    }

    /**
     * Method that returns all the users from the DB
     *
     * @return list of users DTOs
     */
    @Override
    public List<UserDTO> getAllUser() {
        List<UserDTO> listUserDTO = new ArrayList<>();
        List<User> listUser = userRepository.findAll();

        if (!listUser.isEmpty()) {
            for (User user : listUser) {
                if (user.getDeletedAt() == null) {
                    listUserDTO.add(mapper.mapUserToDTO(user));
                }
            }
        }

        return listUserDTO;
    }

    /**
     * Method that returns all the users from the DB
     *
     * @return list of users DTOs
     */
    @Override
    public List<NewDTO> getAllNewsByDasUser(String das) {
        List<NewDTO> listNewsDTO = new ArrayList<>();
        List<News> listNews = newsRepository.findAllNewsByDasUser(das);

        for (News news : listNews) {
            if (news.getDeleted_at() == null) {
                listNewsDTO.add(mapper.mapNewToDTO(news));
            }
        }

        return listNewsDTO;
    }

    /**
     * method that updates the data of the user table
     *
     * @param idUser     ID of the user to update
     * @param updateUser DTO with the data to be updated
     * @return DTO with updated user
     */
    @Override
    public UserDTO updateUser(Long idUser, UserUpdateDTO userUpdateDTO) {
        Optional<User> userOptional = userRepository.findById(idUser);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with ID: " + idUser);
        }
        User user = userOptional.get();

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setRole(userUpdateDTO.getRole());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return mapper.mapUserToDTO(user);
    }

    /**
     * method that disables the logical deletion of a user element from the database
     *
     * @param idUser ID of the user to delete
     */
    @Override
    public void activateUser(Long idUser) {
        Optional<User> userOptional = userRepository.findById(idUser);
        User user = userOptional.get();

        LocalDateTime now = LocalDateTime.now();
        user.setDeletedAt(null);
        user.setUpdatedAt(now);

        userRepository.save(user);
    }

    /**
     * method that performs a logical deletion of a user item from the DB
     *
     * @param idUser ID of the user to delete
     */
    @Override
    public void deactivateUser(Long idUser) {
        Optional<User> userOptional = userRepository.findById(idUser);
        User user = userOptional.get();

        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        user.setDeletedAt(now);
        user.setUpdatedAt(now);
        userRepository.save(user);

        List<News> listNews = newsRepository.findAllNewsByIdUser(idUser);

        for (News news : listNews) {
            if (news.getDeleted_at() == null) {
                news.setDeleted_at(timestamp);
                news.setUpdated_at(timestamp);
                newsRepository.save(news);
            }
        }
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        return das -> userRepository.findByDas(das)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with das: " + das));
    }
}