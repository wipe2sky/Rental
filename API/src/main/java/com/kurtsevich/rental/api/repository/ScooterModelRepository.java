package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.ScooterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScooterModelRepository extends JpaRepository<ScooterModel, Long> {
    Optional<ScooterModel> findByModel(String model);
}
