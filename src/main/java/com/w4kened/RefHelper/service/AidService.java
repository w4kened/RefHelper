package com.w4kened.RefHelper.service;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.AidEntity;

import java.util.List;

public interface AidService {

    List<AidEntity> findAll();

    void saveAid(AidDto aidDto);

    void removeAid(Long usersAidsId, Long aidId);


    AidEntity findByCategoryName(String name);
}
