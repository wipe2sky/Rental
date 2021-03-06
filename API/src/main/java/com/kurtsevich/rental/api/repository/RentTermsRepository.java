package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.RentTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentTermsRepository extends JpaRepository<RentTerms, Long> {
}
