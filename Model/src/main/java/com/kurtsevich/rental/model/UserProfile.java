package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "user_profile")
public class UserProfile extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "prepayments")
    private BigDecimal prepayments;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passport")
    private Passport passport;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "userProfile", fetch = FetchType.LAZY)
    private List<History> histories;
}
