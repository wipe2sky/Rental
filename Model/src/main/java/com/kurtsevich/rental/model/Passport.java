package com.kurtsevich.rental.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "passport")
public class Passport extends BaseEntity {
    @Column(name = "passport_number", nullable = false, unique = true)
    private String passportNumber;

    @Column(name = "identification_number", nullable = false, unique = true)
    private String identificationNumber;

    @Column(name = "date_of_issue", nullable = false)
    private LocalDate dateOfIssue;

    @Column(name = "date_of_expire", nullable = false)
    private LocalDate dateOfExpire;

}
