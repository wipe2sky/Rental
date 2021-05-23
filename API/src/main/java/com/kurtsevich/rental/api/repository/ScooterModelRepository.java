package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.ScooterModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScooterModelRepository extends JpaRepository<ScooterModel, Long> {
    ScooterModel findByModel(String model);
}
