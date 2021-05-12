package com.kurtsevich.rental.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "scooter_model")
public class ScooterModel extends BaseEntity {

    @Column(name = "model")
    private String model;

    @Column(name = "max_speed")
    private int maxSpeed;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "weight_limit")
    private int weightLimit;

    @Column(name = "power")
    private int power;


}
