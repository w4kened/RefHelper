package com.w4kened.RefHelper.service;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import javassist.NotFoundException;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersAidsService {
//    List<AidEntity> findAll();
//
//    void saveUserAids();

    void deleteUsersAidsById(Long id) throws NotFoundException;
    void deleteUsersAidsByAidId(Long id) throws NotFoundException;

    List<UsersAidsEntity> findByAidId(Long id);

    List<UsersAidsEntity> findByAid(Long aid);

//    List<UsersAidsEntity> findByUser

    List<UsersAidsEntity> findByAidInteraction(AidInteraction aidInteraction);
//

//    UserAids
}
