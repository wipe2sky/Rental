package com.kurtsevich.rental.model;

import com.kurtsevich.rental.Status;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "role")
public class Role extends BaseEntity{
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @CreatedDate
    @Column(name = "created")
    private Date created;

    @CreatedDate
    @Column(name = "updated")
    private Date updated;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;
}
