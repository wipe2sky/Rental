package com.kurtsevich.rental.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "history")
public class History extends BaseEntity{
    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "finished")
    private LocalDateTime finished;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "distance")
    private Long distance;

    @Column(name = "actual_discount")
    private int actualDiscount;

    @Column(name = "is_actual")
    private boolean isActual;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scooter")
    private Scooter scooter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile")
    private UserProfile userProfile;
}
