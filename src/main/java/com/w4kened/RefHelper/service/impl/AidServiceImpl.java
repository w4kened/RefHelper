package com.w4kened.RefHelper.service.impl;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.*;
import com.w4kened.RefHelper.repository.AidCategoryRepository;
import com.w4kened.RefHelper.repository.AidRepository;
import com.w4kened.RefHelper.repository.UsersAidsRepository;
import com.w4kened.RefHelper.security.SecurityUtil;
import com.w4kened.RefHelper.service.AidService;
import com.w4kened.RefHelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class AidServiceImpl implements AidService {
    @Autowired
    private  AidRepository aidRepository;
    @Autowired
    private  AidCategoryRepository aidCategoryRepository;
    @Autowired

    private  UserService userService;
    @Autowired
    private  UsersAidsRepository usersAidsRepository;

    @Autowired
    public AidServiceImpl(AidRepository aidRepository,
                          AidCategoryRepository aidCategoryRepository,
                          UserService userService,
                          UsersAidsRepository usersAidsRepository) {
        this.userService = userService;
        this.aidRepository = aidRepository;
        this.aidCategoryRepository = aidCategoryRepository;
        this.usersAidsRepository = usersAidsRepository;
    }


    @Override
    public List<AidEntity> findAll() {
//        save_csv();
        return aidRepository.findAll();
    }


    @Override
    public void saveAid(AidDto aidDto) {
        AidEntity aidEntity = new AidEntity();
        aidEntity.setDescription(aidDto.getDescription());
        aidEntity.setAddress(aidDto.getAddress());
        aidEntity.setLatitude(aidDto.getLatitude());
        aidEntity.setLongitude(aidDto.getLongitude());
//        aidEntity.setCreatedDate(aidDto.getCreatedDate());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTimeString = now.format(formatter);
        LocalDateTime formattedDateTime = LocalDateTime.parse(formattedDateTimeString, formatter);
        aidEntity.setCreatedDate(formattedDateTime);

//        System.out.println("coordinates " + aidEntity.getLatitude() + aidEntity.getLongitude());

        AidCategoryEntity aidCategory;
        if (aidDto.getSelectedCategoryAid() == 1L) {
            aidCategory = aidCategoryRepository.findByName("Basic Necessities Aid");
            aidEntity.setAidCategoryEntity(aidCategory);
        } else if (aidDto.getSelectedCategoryAid() == 2L) {
            aidCategory = aidCategoryRepository.findByName("Healthcare Aid");
            aidEntity.setAidCategoryEntity(aidCategory);
        } else if (aidDto.getSelectedCategoryAid() == 3L) {
            aidCategory = aidCategoryRepository.findByName("Education Aid");
            aidEntity.setAidCategoryEntity(aidCategory);
        } else if (aidDto.getSelectedCategoryAid() == 4L) {
            aidCategory = aidCategoryRepository.findByName("Employment Aid");
            aidEntity.setAidCategoryEntity(aidCategory);
        } else if (aidDto.getSelectedCategoryAid() == 5L) {
            aidCategory = aidCategoryRepository.findByName("Legal Aid");
            aidEntity.setAidCategoryEntity(aidCategory);
        } else if (aidDto.getSelectedCategoryAid() == 6L) {
            aidCategory = aidCategoryRepository.findByName("Community Aid");
            aidEntity.setAidCategoryEntity(aidCategory);
        }
//        System.out.println("saving from service->repository " );
//        System.out.println(aidEntity.getAidCategoryEntity());
        aidRepository.save(aidEntity);
        UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
        UsersAidsEntity usersAidsEntity  = new UsersAidsEntity();
//        List<UsersAidsEntity> usersAidsEntities = usersAidsRepository.findByAidId(aidEntity.getId());
//        Integer count = 1;
//        for (UsersAidsEntity element : usersAidsEntities) {
//            if (element.getAidInteraction() == AidInteraction.CREATING ) {
//                count++;
//            }
////            System.out.println("interaction "+element.getAidInteraction());
//        }

        usersAidsEntity.setAidEntity(aidEntity);
        usersAidsEntity.setUserEntity(userEntity);
        usersAidsEntity.setAidInteraction(AidInteraction.CREATING);
        usersAidsRepository.save(usersAidsEntity);
    }

    @Override
    public void removeAid(Long usersAidsId, Long aidId) {

        usersAidsRepository.removeUsersAidsById(usersAidsId);
        usersAidsRepository.removeUsersAidsById(usersAidsId);
        aidRepository.deleteById(aidId);
    }

    @Override
    public AidEntity findByCategoryName(String name) {
        return null;
    }

    @Override
    public AidEntity findByAidId(Long aidId) {
        return aidRepository.findByAidId(aidId);
    }

    @Override
    public List<AidEntity> findByCreatorUserId(Long userId) {
        return aidRepository.findByCreatorUserId(userId);
    }

//    @Override
//    public List<AidEntity> findByCreator(Long userId) {
//        return aidRepository.findByCreatorUserId(userId);
//    }
}
