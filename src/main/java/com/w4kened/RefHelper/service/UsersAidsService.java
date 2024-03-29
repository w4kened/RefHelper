package com.w4kened.RefHelper.service;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import javassist.NotFoundException;
import org.springframework.data.jpa.repository.Query;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

public interface UsersAidsService {

    void deleteUsersAidsById(Long id) throws NotFoundException;
    void deleteUsersAidsByAidId(Long id) throws NotFoundException;

    List<UsersAidsEntity> findByAidId(Long id);

    List<UsersAidsEntity> findByAid(Long aid);


    List<UsersAidsEntity> findByAidInteraction(AidInteraction aidInteraction);

    List<UsersAidsEntity> findByUserId(Long id);

    List<UsersAidsEntity> findByUserIdAndAidId(Long userId, Long aidId);


    List<UsersAidsEntity> findResponsesByUserId(Long userId) throws NotFoundException;

    Map<String, Long> getOverallDataForChart();

    Map<String, Long> getMostRequestedDataForChart();
}
