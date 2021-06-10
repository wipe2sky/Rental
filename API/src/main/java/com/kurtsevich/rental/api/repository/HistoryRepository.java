package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.History;
import com.kurtsevich.rental.model.Scooter;
import com.kurtsevich.rental.model.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query(value = "SELECT h FROM History h WHERE h.userProfile = ?1 and h.isActual = 1")
    History findActualHistory(UserProfile userProfile);

    List<History> findAllByUserProfile(UserProfile userProfile, Pageable page);

    List<History> findAllByScooter(Scooter scooter, Pageable page);

    @Query(value = "SELECT h FROM History h WHERE h.created > ?1 and h.created < ?2 ORDER BY h.created")
    List<History> findAllByDate(LocalDateTime startDate, LocalDateTime finishedDate, Pageable page);
}
