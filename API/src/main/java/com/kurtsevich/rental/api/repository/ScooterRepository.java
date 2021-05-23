package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.model.RentalPoint;
import com.kurtsevich.rental.model.Scooter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScooterRepository extends JpaRepository<Scooter, Long> {

    @Query(value = "SELECT s FROM Scooter s WHERE s.rentalPoint = ?1 and s.status = ?2 ORDER BY id")
    Page<Scooter> findAllByRentalPointAndStatusWithPagination(RentalPoint rentalPoint, Status status, Pageable page);

    int countByRentalPointAndStatus(RentalPoint rentalPoint, Status status);



}
