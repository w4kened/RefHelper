package com.w4kened.RefHelper.service;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersAidsService {
//    List<AidEntity> findAll();
//
//    void saveUserAids();

    List<UsersAidsEntity> findByAidId(Long id);

    List<UsersAidsEntity> findByAid(Long aid);

    List<UsersAidsEntity> findByAidInteraction(AidInteraction aidInteraction);
//

//    UserAids
}
