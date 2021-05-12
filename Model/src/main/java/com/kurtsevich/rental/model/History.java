package com.kurtsevich.rental.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "history")
public class History extends BaseEntity{
    @CreatedDate
    @Column(name = "created")
    private Date created;

    @CreatedDate
    @Column(name = "finished")
    private Date finished;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "actual_discount")
    private BigDecimal actualDiscount;

    @Column(name = "is_actual")
    private boolean isActual;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scooter")
    private Scooter scooter;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile")
    private UserProfile userProfile;
}
