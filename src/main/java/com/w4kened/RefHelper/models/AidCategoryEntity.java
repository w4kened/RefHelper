package com.w4kened.RefHelper.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "aidCategory_table")
public class AidCategoryEntity {

    public AidCategoryEntity(String name, Long id) {
        this.id = id;
        this.name = name;
    }

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "aidCategoryEntity")
    @JsonBackReference
    private List<AidEntity> aidEntities;
}
