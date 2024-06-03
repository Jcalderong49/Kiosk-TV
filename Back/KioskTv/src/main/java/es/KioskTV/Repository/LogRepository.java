package es.KioskTV.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.KioskTV.entity.Log;

/**
 * It is an interface of the Log entity that extends JpaRepository for its
 * methods
 */
@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}