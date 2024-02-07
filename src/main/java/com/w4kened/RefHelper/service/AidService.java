package com.w4kened.RefHelper.service;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.RegionEntity;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

public interface AidService {

    List<RegionEntity> getRegions();

    List<AidEntity> findAll();

    List<UsersAidsEntity> findRequestedAidsByAidIds(List<Long> id) throws NotFoundException;

    boolean saveAid(AidDto aidDto);

    boolean updateAid(AidDto aidDto, Long id) throws NotFoundException;


    boolean deleteAidById(Long id) throws NotFoundException;

    AidEntity findByCategoryName(String name);

    AidEntity findByAidId(Long aidId);

    List<AidEntity> findByCreatorUserId(Long userId);

    List<AidEntity> findByRequesterUserId(Long userId);

//    public Page<AidEntity> findByCreatorUserId(Long userId, Pageable pageable);



    boolean requestAid(Long aidId) throws NotFoundException;
    void acceptAidRequest(Long aidId, Long userId) throws NotFoundException;

    void rejectAidRequest(Long aidId, Long userId) throws NotFoundException;

    Long countRequestedAidByUser(Long userId) throws NotFoundException;
    Long countAcceptedAidByUser(Long userId) throws NotFoundException;

    Map<String, Long> getRegionalDistributionOfAidsForChart();

}
