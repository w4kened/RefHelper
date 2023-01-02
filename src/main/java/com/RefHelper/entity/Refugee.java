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

    public Long getRefugee_id() {
        return refugee_id;
    }

    public void setRefugee_id(Long refugee_id) {
        this.refugee_id = refugee_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<Aid> getAids() {
        return aids;
    }

    public void setAids(Set<Aid> aids) {
        this.aids = aids;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}