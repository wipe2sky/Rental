package com.kurtsevich.rental.api.repository;

import com.kurtsevich.rental.model.RentTerms;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RentTermsRepository extends JpaRepository<RentTerms, Long> {
}
