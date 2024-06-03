package es.KioskTV.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import es.KioskTV.entity.News;

/**
 * It is an interface of the New entity that extends JpaRepository for its
 * methods
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("select n from News n where n.user.das=?1")
    List<News> findAllNewsByDasUser(String das);

    @Query("select n from News n where n.user.id=?1")
    List<News> findAllNewsByIdUser(Long id);
}