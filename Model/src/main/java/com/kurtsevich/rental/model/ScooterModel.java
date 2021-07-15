package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
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
    private Integer weightLimit;

    @Column(name = "power", nullable = false)
    private Integer power;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScooterModel)) return false;
        if (!super.equals(o)) return false;
        ScooterModel that = (ScooterModel) o;
        return maxSpeed == that.maxSpeed && weightLimit == that.weightLimit && power == that.power && Objects.equals(model, that.model) && Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, maxSpeed, weight, weightLimit, power);
    }
}
