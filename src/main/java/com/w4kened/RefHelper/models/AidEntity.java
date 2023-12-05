package com.w4kened.RefHelper.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "aid_table")
public class AidEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "latitude", precision = 10, scale = 6) // Adjust precision and scale accordingly
    private Double latitude;


    @Column(name = "longitude", precision = 10, scale = 6) // Adjust precision and scale accordingly
    private Double longitude;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private AidCategoryEntity aidCategoryEntity;

    @OneToMany(mappedBy = "aidEntity", cascade=CascadeType.ALL,orphanRemoval=true)
    @JsonManagedReference
    private List<UsersAidsEntity> usersAidsEntities;
}
