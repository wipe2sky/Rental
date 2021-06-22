package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "scooter")
public class Scooter extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "charge", nullable = false)
    private int charge;

    @Column(name = "mileage", nullable = false)
    private Long mileage;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scooter_model", nullable = false)
    private ScooterModel scooterModel;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_point")
    private RentalPoint rentalPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_terms")
    private RentTerms rentTerms;

    @ToString.Exclude
    @OneToMany(mappedBy = "scooter", fetch = FetchType.LAZY)
    private List<History> histories;
}
