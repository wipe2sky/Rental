package com.kurtsevich.rental.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "rental_point")
public class RentalPoint extends BaseEntity{
    @Column(name = "name")
    private String name;

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
    private int phoneNumber;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "latitude")
    private Float latitude;

    @OneToMany(mappedBy = "rentalPoint", fetch = FetchType.LAZY)
    private List<Scooter> scooters;
}
