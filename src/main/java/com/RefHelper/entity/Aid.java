package com.RefHelper.entity;

import com.RefHelper.entity.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.sql.Ref;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import org.hibernate.annotations.CascadeType;



@Getter
@Setter
@Entity
@Table(name = "aid")
public class Aid implements Serializable {

    private static final long serialVersiosnUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", columnDefinition = "text", nullable = false)
    private String title;

    @Column(name = "amountOfFollowers", nullable = false)
    private int amountOfFollowers;

    @Column(name = "town", columnDefinition = "text", nullable = false)
    private String town;

    @Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    @JsonBackReference
    private Volunteer volunteer;

    @ManyToMany(mappedBy = "aids")
    private Set<Refugee> refugees = new HashSet<>();

    //FIXME.
    //object references an unsaved transient instance - save the transient instance before flushing : com.RefHelper.entity.Aid.category -> com.RefHelper.entity.Category
    @ManyToOne
    //@Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "category_id", nullable = true)
    @JsonBackReference
    private Category category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "geom", length = 65555, nullable = false)
    private Point geom;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public int getAmountOfFollowers() {
        return amountOfFollowers;
    }

    public void setAmountOfFollowers(int amountOfFollowers) {
        this.amountOfFollowers = amountOfFollowers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Set<Refugee> getRefugees() {
        return refugees;
    }

    public void setRefugees(Set<Refugee> refugees) {
        this.refugees = refugees;
    }

    public void addRefugee(Refugee refugee) {
        refugees.add(refugee);
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }


    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Point getGeom() {
        return geom;
    }

    public void setGeom(Point geom) {
        this.geom = geom;
    }
}
