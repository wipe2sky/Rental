package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

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
import java.util.Date;
import java.util.List;

@Entity
@Data
@ToString(exclude = "user")
@Table(name = "user_profile")
public class UserProfile extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @CreatedDate
    @Column(name = "created")
    private Date created;

    @CreatedDate
    @Column(name = "updated")
    private Date updated;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "prepayments")
    private BigDecimal prepayments;

    @OneToOne
    @JoinColumn(name = "passport")
    private Passport passport;

    @OneToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "userProfile", fetch = FetchType.LAZY)
    private List<History> histories;
}
