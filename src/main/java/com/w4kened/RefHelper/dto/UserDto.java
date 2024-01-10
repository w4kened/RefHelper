package com.w4kened.RefHelper.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Email;
import javax.persistence.*;

import com.w4kened.RefHelper.models.UserEntity;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDto {

    public UserDto (UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.firstName = userEntity.getName().split(" ")[0];
        this.lastName = userEntity.getName().split(" ")[1];
        this.cityName = userEntity.getCityEntity().getName();
        this.selectedRole = Math.toIntExact(userEntity.getRoleEntity().getId());
        this.phoneNumber = userEntity.getPhoneNumber();
    }

    @Email
    @NotEmpty
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    @NotEmpty(message = "Password should not be empty")
    private String repeatPassword;


    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private String cityName;

    private Integer selectedRole;

    public UserDto() {
        this.selectedRole = 2;
    }
}
