package es.KioskTV.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.KioskTV.Repository.UserRepository;
import es.KioskTV.entity.Role;
import es.KioskTV.entity.User;

/**
 * This component initializes data for the application upon startup.
 * It implements CommandLineRunner to execute custom logic after the application
 * context is loaded.
 */
@Component
public class InicializarDatos implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Executes the data initialization logic.
     *
     * @param args the command-line arguments
     * @throws Exception if an error occurs during data initialization
     */
    @Override
    public void run(String... args) throws Exception {
        insertUserIfNoExist("adminDas", "Admin", "admin", "admin1234", Role.ROL_ADMIN);
        insertUserIfNoExist("userDas", "User", "user", "user1234", Role.ROL_USER);
    }

    /**
     * Inserts a user into the database if it does not already exist.
     *
     * @param das       the DAS of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param password  the password of the user
     * @param role      the role of the user
     */
    private void insertUserIfNoExist(String das, String firstName, String lastName, String password, Role role) {
        userRepository.findByDas(das).orElseGet(() -> {
            User user = new User();
            user.setDas(das);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            return userRepository.save(user);
        });
    }
}
