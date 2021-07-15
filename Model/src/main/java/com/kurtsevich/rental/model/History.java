package com.kurtsevich.rental.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "history")
public class History extends BaseEntity {
    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "finished")
    private LocalDateTime finished;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "distance")
    private Long distance;

    @Column(name = "actual_discount")
    private Integer actualDiscount;

    @Column(name = "is_actual", nullable = false)
    private boolean isActual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scooter", nullable = false)
    private Scooter scooter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile", nullable = false)
    private UserProfile userProfile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof History)) return false;
        if (!super.equals(o)) return false;
        History history = (History) o;
        return Objects.equals(created, history.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), created);
    }
}
