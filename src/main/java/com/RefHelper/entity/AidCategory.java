package com.RefHelper.entity;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.lang.NumberFormatException;
import com.RefHelper.entity.enums.ECategoryHelp;


@Data
@Entity
@Table(name = "aid_category_table")
public class AidCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    @Enumerated(EnumType.STRING)
    private ECategoryHelp categories;

    @Column(columnDefinition = "text")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy= "category",
        orphanRemoval = true)
    private List<Aid> aid = new ArrayList<>();

    private LocalDateTime createdDate;

    public AidCategory() {}

    public AidCategory(String cat_arg) {


        int result = Integer.parseInt(cat_arg);

        switch(result) {
            case 0:
                this.categories = ECategoryHelp.CATEGORY_HUMANITARIAN;
                break;
            case 1:
                this.categories = ECategoryHelp.CATEGORY_ACCOMMODATION;
                break;
            case 2:
                this.categories = ECategoryHelp.CATEGORY_HEALTHCARE;
                break;
            case 3:
                this.categories = ECategoryHelp.CATEGORY_JOB;
                break;
            case 4:
                this.categories = ECategoryHelp.CATEGORY_EDUCATION;
                break;
            case 5:
                this.categories = ECategoryHelp.CATEGORY_LEGALASSISTANCE;
                break;
            default:
                System.out.println("No such category or string is errorous");
                break;
        }

    }


    //zaimplementować
     public  static AidCategory valueOf(String s_categoryHelp) {
        return new AidCategory(s_categoryHelp);
     }


    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }



    public Long getId() {
        return category_id;
    }

    public void setId(Long id) {
        this.category_id = id;
    }

    public ECategoryHelp getCategories() {
        return categories;
    }

    public void setCategories(ECategoryHelp categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Aid> getAid() {
        return aid;
    }

    public void setAid(List<Aid> aid) {
        this.aid = aid;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }



}
