package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.History;
import com.kurtsevich.rental.model.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    @EntityGraph(attributePaths = {"scooter"})
    History findHistoryByUserProfileAndIsActualIsTrue(UserProfile userProfile);

    @Query(value = "SELECT h FROM History h INNER JOIN h.userProfile up  INNER JOIN up.user u WHERE u.username = ?1")
    List<History> findAllByUsername(String username, Pageable page);

    List<History> findAllByScooterId(Long id, Pageable page);

    @Query(value = "SELECT h FROM History h WHERE h.created > ?1 and h.created < ?2 ORDER BY h.created")
    List<History> findAllByDate(LocalDateTime startDate, LocalDateTime finishedDate, Pageable page);
}
