package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.model.Scooter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScooterRepository extends JpaRepository<Scooter, Long> {

    Page<Scooter> findAllByRentalPointIdAndStatusOrderById(Long rentalPointId, Status status, Pageable page);

    int countByRentalPointIdAndStatus(Long rentalPointId, Status status);

    Page<Scooter> findAllByScooterModelId(Long scooterModelId, Pageable page);


}
