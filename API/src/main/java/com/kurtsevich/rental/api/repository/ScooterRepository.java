package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScooterRepository extends JpaRepository<Scooter, Long> {
}
