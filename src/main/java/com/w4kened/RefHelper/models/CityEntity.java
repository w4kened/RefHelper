package com.w4kened.RefHelper.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "city_table")
public class CityEntity {

    public CityEntity (String name, Long id) {
        this.name = name;
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @OneToMany(mappedBy = "cityEntity", cascade= CascadeType.ALL,orphanRemoval=true)
    @JsonBackReference
    private List<UserEntity> userEntities;

    @ManyToOne
    @JoinColumn(name = "region_id")
    @JsonManagedReference
    private RegionEntity regionEntity;
}
