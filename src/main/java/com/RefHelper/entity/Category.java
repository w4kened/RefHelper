package com.RefHelper.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.NotFound;

import com.RefHelper.entity.enums.ECategoryHelp;

@Data
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_e", nullable = false)
    private ECategoryHelp category_e;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy= "category",  fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<Aid> aids = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "createdDate", updatable = false)
    private LocalDateTime createdDate;

    public Category() {}

    public Category(String id) {
        this.id = Integer.parseInt(id);
/*
        switch (result) {
            case 1:
                this.category_e = ECategoryHelp.CATEGORY_HUMANITARIAN;
                break;
            case 2:
                this.category_e = ECategoryHelp.CATEGORY_ACCOMMODATION;
                break;
            case 3:
                this.category_e = ECategoryHelp.CATEGORY_HEALTHCARE;
                break;
            case 4:
                this.category_e = ECategoryHelp.CATEGORY_JOB;
                break;
            case 5:
                this.category_e = ECategoryHelp.CATEGORY_EDUCATION;
                break;
            case 6:
                this.category_e = ECategoryHelp.CATEGORY_LEGALASSISTANCE;
                break;
            default:
                System.out.println("No such category or string is errorous");
                break;
        }

*/
    }


    //zaimplementować
     public static Category valueOf(String id) {
        return new Category(id);
     }


    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ECategoryHelp getCategory() {
        return category_e;
    }

    public void setCategory(ECategoryHelp categories) {
        this.category_e = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Aid> getAid() {
        return aids;
    }

    public void setAid(List<Aid> aid) {
        this.aids = aid;
    }

    public void addAid(Aid aid) {
        aids.add(aid);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

}
