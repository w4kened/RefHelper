package com.RefHelper.entity;

import com.RefHelper.entity.enums.ECategoryHelp;
import com.RefHelper.entity.enums.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity

public class Aid implements  Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Long volunteerId;

    @ElementCollection(targetClass = ECategoryHelp.class)
    @CollectionTable(name = "caterogy_help", joinColumns = @JoinColumn("aid_id"))
    private final Set<ECategoryHelp> categoryHelp = new HashSet<>();

    @JsonFormat(pattern = "dd-mm-yyyy HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private Point geom;


    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
