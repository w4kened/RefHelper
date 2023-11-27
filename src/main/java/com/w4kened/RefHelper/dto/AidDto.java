package com.w4kened.RefHelper.dto;

import com.w4kened.RefHelper.models.AidEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class AidDto {
    private int selectedCategoryAid;

    String description;

    @NotEmpty
    @Column(name = "latitude", precision = 10, scale = 6) // Adjust precision and scale accordingly
    private Double latitude;

    @NotEmpty
    @Column(name = "longitude", precision = 10, scale = 6) // Adjust precision and scale accordingly
    private Double longitude;


    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

}
