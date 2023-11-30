package com.w4kened.RefHelper.models;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role_table")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "roleEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<UserEntity> userEntities;
}
