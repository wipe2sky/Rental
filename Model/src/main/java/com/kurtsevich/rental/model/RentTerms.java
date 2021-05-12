package com.kurtsevich.rental.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "rent_terms")
public class RentTerms extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "time_in_seconds")
    private Long timeInSeconds;

    @Column(name = "price")
    private BigDecimal price;

}
