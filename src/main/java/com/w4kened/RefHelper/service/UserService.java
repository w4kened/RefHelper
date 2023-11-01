package com.w4kened.RefHelper.service;

import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.UserEntity;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    UserEntity findByEmail(String email);
//    List<UserDto> findAllUsers();
}
