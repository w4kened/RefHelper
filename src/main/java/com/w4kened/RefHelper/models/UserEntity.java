package com.w4kened.RefHelper.models;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_table")
public class UserEntity {
    private static final long serialVersionUID = 1L;

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

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "users_aids",
//            joinColumns = {
//                    @JoinColumn(name = "user_id", referencedColumnName = "id")
//            },
//            inverseJoinColumns = {
//                    @JoinColumn(name = "aid_id", referencedColumnName = "id")
//            }
//    )
//    private List<AidEntity> aidEntities = new ArrayList<>();
    @OneToMany(mappedBy = "userEntity")
    @JsonBackReference
    private List<UsersAidsEntity> usersAidsEntities;
}
