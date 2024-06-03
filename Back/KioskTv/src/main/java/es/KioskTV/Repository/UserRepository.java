package es.KioskTV.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.KioskTV.entity.User;

/**
 * It is an interface of the User entity that extends JpaRepository for its
 * methods
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDas(String das);
}