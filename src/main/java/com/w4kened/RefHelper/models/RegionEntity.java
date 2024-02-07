package com.w4kened.RefHelper.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import com.w4kened.RefHelper.models.CityEntity;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "region_table")
public class RegionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "regionEntity", cascade= CascadeType.ALL,orphanRemoval=true)
    @JsonBackReference
    private List<CityEntity> cityEntities;
}
