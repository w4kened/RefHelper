package com.w4kened.RefHelper.service;

import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.RoleEntity;
import com.w4kened.RefHelper.models.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    void saveUser(UserDto userDto);
    UserEntity findByEmail(String email);
    Map<String, Long> getRegionalDistributionOfRefugeesForChart();


//    RoleEntity getRoleOfUser(String email);
//    List<UserDto> findAllUsers();
}
