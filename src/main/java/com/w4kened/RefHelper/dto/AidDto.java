package com.w4kened.RefHelper.dto;

import com.w4kened.RefHelper.models.AidEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class AidDto {

    public AidDto(AidEntity aidEntity) {
        this.selectedCategoryAid = aidEntity.getAidCategoryEntity().getId();
        this.description = aidEntity.getDescription();
        this.latitude = aidEntity.getLatitude();
        this.longitude = aidEntity.getLongitude();
        this.address = aidEntity.getAddress();
        this.id = aidEntity.getId();
    }

    public AidDto(AidEntity aidEntity, String refugeeName, String refugeePhoneNumber, String refugeeEmail) {
        this.selectedCategoryAid = aidEntity.getAidCategoryEntity().getId();
        this.description = aidEntity.getDescription();
        this.latitude = aidEntity.getLatitude();
        this.longitude = aidEntity.getLongitude();
        this.address = aidEntity.getAddress();
        this.id = aidEntity.getId();
        this.refugeeName = refugeeName;
        this.refugeePhoneNumber = refugeePhoneNumber;
        this.refugeeEmail = refugeeEmail;
    }


    @NotEmpty
    private Long selectedCategoryAid;

    @NotEmpty
    String description;

//    @NotEmpty
    @Column(name = "latitude", precision = 10, scale = 6) // Adjust precision and scale accordingly
    private Double latitude;

//    @NotEmpty
    @Column(name = "longitude", precision = 10, scale = 6) // Adjust precision and scale accordingly
    private Double longitude;


//    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private String address;

    private AidEntity aidEntity;
    private Long id;

    String refugeeName;
    String refugeePhoneNumber;
    String refugeeEmail;
}
