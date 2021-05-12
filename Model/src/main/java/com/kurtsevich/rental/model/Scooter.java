package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "scooter")
public class Scooter extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "charge")
    private int charge;

    @CreatedDate
    @Column(name = "created")
    private Date created;

    @CreatedDate
    @Column(name = "updated")
    private Date updated;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scooter_model", nullable = false)
    private ScooterModel scooterModel;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_point")
    private RentalPoint rentalPoint;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "scooter_rent_terms",
            joinColumns = {@JoinColumn(name = "scooter_id")},
            inverseJoinColumns = {@JoinColumn(name = "rent_terms_id")})
    private List<RentTerms> rentTermsList;

    @OneToMany(mappedBy = "scooter", fetch = FetchType.LAZY)
    private List<History> histories;
}
