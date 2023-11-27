package com.w4kened.RefHelper.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users_aids_table")
public class UsersAidsEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "aid_id")
    @JsonBackReference
    private AidEntity aidEntity;

    @Enumerated(EnumType.STRING)
    private AidInteraction aidInteraction;
}
