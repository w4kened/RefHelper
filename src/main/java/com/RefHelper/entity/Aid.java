package com.RefHelper.entity;

import com.RefHelper.entity.AidCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.sql.Ref;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Getter
@Data
@Entity
@Table(name = "aid_table")
public class Aid implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aid_id;

    @Column(name = "aid_title", columnDefinition = "text")
    private String aid_title;

    @Column(name = "aid_amountOfFollowers")
    private int aid_amountOfFollowers;

    @Column(name = "town", columnDefinition = "text")
    private String aid_town;

    @Column(columnDefinition = "text")
    private String aid_description;

    /*
    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> subscribedRef = new HashSet<>();
*/

    @ManyToOne(fetch = FetchType.LAZY)
    private Volunteer volunteer;

    @ManyToMany(mappedBy = "aids")
    private Set<Refugee> refugees = new HashSet<>();


/*

    @ElementCollection(targetClass = ECategoryHelp.class)
    @CollectionTable(name = "aid_category",
            joinColumns = @JoinColumn(name="aid_id"))
    private final Set<ECategoryHelp> categoryHelp = new HashSet<>();
*/

    @ManyToOne(fetch = FetchType.LAZY)
    private AidCategory category;

    @JsonFormat(pattern = "dd-mm-yyyy HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "geom")
    private Point geom;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    public Long getAid_id() {
        return aid_id;
    }

    public void setAid_id(Long aid_id) {
        this.aid_id = aid_id;
    }

    public String getAid_title() {
        return aid_title;
    }

    public AidCategory getCategory() {
        return category;
    }

    public void setCategory(AidCategory category) {
        this.category = category;
    }

    public void setAid_title(String aid_title) {
        this.aid_title = aid_title;
    }

    public int getAid_amountOfFollowers() {
        return aid_amountOfFollowers;
    }

    public void setAid_amountOfFollowers(int aid_amountOfFollowers) {
        this.aid_amountOfFollowers = aid_amountOfFollowers;
    }

    public String getAid_description() {
        return aid_description;
    }

    public void setAid_description(String aid_description) {
        this.aid_description = aid_description;
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


    public String getTown() {
        return aid_town;
    }

    public void setTown(String town) {
        this.aid_town = town;
    }


    public void setRefugees(Set<Refugee> refugees) {
        this.refugees = refugees;
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

    public String getAid_town() {
        return aid_town;
    }

    public void setAid_town(String aid_town) {
        this.aid_town = aid_town;
    }

}
