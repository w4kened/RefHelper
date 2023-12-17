package com.w4kened.RefHelper.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.w4kened.RefHelper.models.RegionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "city_table")
public class CityEntity {
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
