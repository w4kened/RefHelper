package com.w4kened.RefHelper.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Email;
import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String phoneNumber;

    private int selectedRole;

    public UserDto() {
        this.selectedRole = 2;
    }
}
