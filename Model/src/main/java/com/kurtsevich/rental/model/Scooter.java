package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
import java.time.LocalDateTime;
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

    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "updated")
    private LocalDateTime updated;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scooter_model", nullable = false)
    private ScooterModel scooterModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_point")
    private RentalPoint rentalPoint;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "scooter_rent_terms",
            joinColumns = {@JoinColumn(name = "scooter_id")},
            inverseJoinColumns = {@JoinColumn(name = "rent_terms_id")})
    private List<RentTerms> rentTermsList;

    @OneToMany(mappedBy = "scooter", fetch = FetchType.LAZY)
    private List<History> histories;
}
