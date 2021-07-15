package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "rental_point")
public class RentalPoint extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street_name", nullable = false)
    private String streetName;

    @Column(name = "street_type", nullable = false)
    private String streetType;

    @Column(name = "street_number", nullable = false)
    private Integer streetAddressNumber;

    @Column(name = "street_number_suffix")
    private Character streetAddressNumberSuffix;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @OneToMany(mappedBy = "rentalPoint", fetch = FetchType.LAZY)
    private List<Scooter> scooters;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RentalPoint)) return false;
        if (!super.equals(o)) return false;
        RentalPoint that = (RentalPoint) o;
        return streetAddressNumber == that.streetAddressNumber && streetAddressNumberSuffix == that.streetAddressNumberSuffix && Double.compare(that.longitude, longitude) == 0 && Double.compare(that.latitude, latitude) == 0 && Objects.equals(name, that.name) && Objects.equals(city, that.city) && Objects.equals(streetName, that.streetName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, streetName, streetAddressNumber, streetAddressNumberSuffix, longitude, latitude);
    }
}
