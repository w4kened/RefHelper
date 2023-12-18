package com.w4kened.RefHelper.service;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

public interface AidService {

    List<AidEntity> findAll();

    List<UsersAidsEntity> findRequestedAidsByAidIds(List<Long> id) throws NotFoundException;

//    findResponsesByUserId

    void saveAid(AidDto aidDto);

    void updateAid(AidDto aidDto, Long id) throws NotFoundException;

//    void removeAid(Long usersAidsId, Long aidId);

    void deleteAidById(Long id) throws NotFoundException;

    AidEntity findByCategoryName(String name);

    AidEntity findByAidId(Long aidId);

    List<AidEntity> findByCreatorUserId(Long userId);

    void requestAid(Long aidId) throws NotFoundException;
    void acceptAidRequest(Long aidId, Long userId) throws NotFoundException;

    void rejectAidRequest(Long aidId, Long userId) throws NotFoundException;

    Long countRequestedAidByUser(Long userId) throws NotFoundException;
    Long countAcceptedAidByUser(Long userId) throws NotFoundException;

}
