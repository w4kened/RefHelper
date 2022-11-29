package com.RefHelper.entity;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "volunteer_table")
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true, updatable = true)
    private String username;

    @Column(length = 3000)
    private String password;

    @Column(length = 9)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    @Column(name = "score")
    private int score;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "volunteer",
            orphanRemoval = true)
    private List<Aid> aid  = new ArrayList<>();

    private LocalDateTime createdDate;



    public Volunteer() {}

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }


}
