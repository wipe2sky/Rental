package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "scooter_model")
public class ScooterModel extends BaseEntity {

    @Column(name = "model")
    private String model;

    @Column(name = "name")
    private String name;

    @Column(name = "max_speed")
    private int maxSpeed;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "weight_limit")
    private int weightLimit;

    @Column(name = "power")
    private int power;


}
