package com.RefHelper.entity;

import com.RefHelper.entity.enums.ECategoryHelp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.sql.Ref;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

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


    @ElementCollection(targetClass = ECategoryHelp.class)
    @CollectionTable(name = "aid_category",
            joinColumns = @JoinColumn(name="aid_id"))
    private final Set<ECategoryHelp> categoryHelp = new HashSet<>();

    @JsonFormat(pattern = "dd-mm-yyyy HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "geom")
    private Point geom;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
