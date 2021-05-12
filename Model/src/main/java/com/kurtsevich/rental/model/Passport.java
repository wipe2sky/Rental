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
    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "date_of_issue")
    private LocalDate dateOfIssue;

    @Column(name = "date_of_expire")
    private LocalDate dateOfExpire;

}