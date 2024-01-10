package com.w4kened.RefHelper.models;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user_table")
public class UserEntity {
    private static final long serialVersionUID = 1L;

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserEntity(String email, String password, RoleEntity roleEntity) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "createdDate" ,updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonManagedReference
    private RoleEntity roleEntity;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonManagedReference
    private CityEntity cityEntity;

    @OneToMany(mappedBy = "userEntity")
    @JsonBackReference
    private List<UsersAidsEntity> usersAidsEntities;
}
