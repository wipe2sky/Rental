package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.History;
import com.kurtsevich.rental.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUserProfile(UserProfile userProfile);
    History findActualIsTrueByUserProfile(UserProfile userProfile);
}
