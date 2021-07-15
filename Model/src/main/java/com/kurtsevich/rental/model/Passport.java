package com.kurtsevich.rental.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passport)) return false;
        if (!super.equals(o)) return false;
        Passport passport = (Passport) o;
        return Objects.equals(passportNumber, passport.passportNumber) && Objects.equals(identificationNumber, passport.identificationNumber) && Objects.equals(dateOfIssue, passport.dateOfIssue) && Objects.equals(dateOfExpire, passport.dateOfExpire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber, identificationNumber, dateOfIssue, dateOfExpire);
    }
}
