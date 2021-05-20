package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {
}
