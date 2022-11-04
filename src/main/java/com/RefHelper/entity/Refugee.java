package com.RefHelper.entity;

import com.RefHelper.entity.enums.ERole;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class Refugee {
    private Long id;
    private String name;
    private String lastname;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String bio;

    private Set<ERole> role = new HashSet<>();
    private List<Aid> aids = new ArrayList<>();
    private LocalDateTime performedDate;

    @PrePersist
    protected void onCreate() {
        this.performedDate = LocalDateTime.now();
    }
}