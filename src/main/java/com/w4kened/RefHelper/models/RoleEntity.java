package com.w4kened.RefHelper.models;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "role_table")
public class RoleEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "roleEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<UserEntity> userEntities;


    public RoleEntity(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return this.name; // Provide the authority name
    }
}
