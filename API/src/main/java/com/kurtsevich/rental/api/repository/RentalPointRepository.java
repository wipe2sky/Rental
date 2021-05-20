package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.RentalPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalPointRepository extends JpaRepository<RentalPoint, Long> {
}
