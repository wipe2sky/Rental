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

    @Column(name = "model", nullable = false, unique = true)
    private String model;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_speed", nullable = false)
    private int maxSpeed;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "weight_limit", nullable = false)
    private int weightLimit;

    @Column(name = "power", nullable = false)
    private int power;


}
