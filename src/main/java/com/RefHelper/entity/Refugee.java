package com.RefHelper.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@Table(name = "refugee_table")
public class Refugee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refugee_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(length = 3000)
    private String password;

    @Column(length = 9)
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String bio;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Refugee_Aid_table",
            joinColumns = { @JoinColumn(name = "refugee_id") },
            inverseJoinColumns = { @JoinColumn(name = "aid_id") }
    )
    Set<Aid> aids = new HashSet<>();


    @JsonFormat(pattern = "dd-mm-yyyy HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Refugee() {}

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}