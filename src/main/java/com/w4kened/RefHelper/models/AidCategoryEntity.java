package com.w4kened.RefHelper.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "aidCategory_table")
public class AidCategoryEntity {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "aidCategoryEntity")
    @JsonBackReference
    private List<AidEntity> aidEntities;
}
