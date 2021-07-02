package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.History;
import com.kurtsevich.rental.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    @EntityGraph(attributePaths = {"scooter"})
    Optional<History> findHistoryByUserProfileAndIsActualIsTrue(UserProfile userProfile);

    @Query(value = "SELECT h FROM History h INNER JOIN h.userProfile up  INNER JOIN up.user u WHERE u.username = ?1")
    Page<History> findAllByUsername(String username, Pageable page);

    Page<History> findAllByScooterId(Long id, Pageable page);

    @Query(value = "SELECT h FROM History h WHERE h.created > ?1 and h.created < ?2 ORDER BY h.created")
    Page<History> findAllByDate(LocalDateTime startDate, LocalDateTime finishedDate, Pageable page);
}
