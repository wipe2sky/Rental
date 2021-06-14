package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "rental_point")
public class RentalPoint extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "city")
    private String city;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "street_type")
    private String streetType;

    @Column(name = "street_number")
    private int streetAddressNumber;

    @Column(name = "street_number_suffix")
    private char streetAddressNumberSuffix;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "longitude")
    private double longitude;
    @Column(name = "latitude")
    private double latitude;


    @OneToMany(mappedBy = "rentalPoint", fetch = FetchType.LAZY)
    private List<Scooter> scooters;
}
