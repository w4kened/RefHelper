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
import java.util.List;

@Service
public class AidServiceImpl implements AidService {
    @Autowired
    private final AidRepository aidRepository;
    @Autowired
    private final AidCategoryRepository aidCategoryRepository;
    @Autowired

    private final UserService userService;
    @Autowired
    private final UsersAidsRepository usersAidsRepository;

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
        aidEntity.setLatitude(aidDto.getLatitude());
        aidEntity.setLongitude(aidDto.getLongitude());
        aidEntity.setCreatedDate(aidDto.getCreatedDate());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTimeString = now.format(formatter);
        LocalDateTime formattedDateTime = LocalDateTime.parse(formattedDateTimeString, formatter);
        aidEntity.setCreatedDate(formattedDateTime);

        System.out.println("coordinates " + aidEntity.getLatitude() + aidEntity.getLongitude());

        AidCategoryEntity aidCategory;
        switch (aidDto.getSelectedCategoryAid()) {
            case 1 -> {
                aidCategory = aidCategoryRepository.findByName("Basic Necessities Aid");
                aidEntity.setAidCategoryEntity(aidCategory);
            }
            case 2 -> {
                aidCategory = aidCategoryRepository.findByName("Healthcare Aid");
                aidEntity.setAidCategoryEntity(aidCategory);
            }
            case 3 -> {
                aidCategory = aidCategoryRepository.findByName("Education Aid");
                aidEntity.setAidCategoryEntity(aidCategory);
            }
            case 4 -> {
                aidCategory = aidCategoryRepository.findByName("Employment Aid");
                aidEntity.setAidCategoryEntity(aidCategory);
            }
            case 5 -> {
                aidCategory = aidCategoryRepository.findByName("Legal Aid");
                aidEntity.setAidCategoryEntity(aidCategory);
            }
            case 6 -> {
                aidCategory = aidCategoryRepository.findByName("Community Aid");
                aidEntity.setAidCategoryEntity(aidCategory);
            }
        }
        System.out.println("saving from service->repository " );
        System.out.println(aidEntity);
        aidRepository.save(aidEntity);

        UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
        UsersAidsEntity usersAidsEntity  = new UsersAidsEntity();
        usersAidsEntity.setAidEntity(aidEntity);
        usersAidsEntity.setUserEntity(userEntity);
        usersAidsEntity.setAidInteraction(AidInteraction.CREATING);
        usersAidsRepository.save(usersAidsEntity);
    }

    @Override
    public void removeAid(Long usersAidsId, Long aidId) {

//        AidEntity aidEntity = new AidEntity();
//        aidEntity.setDescription(aidDto.getDescription());
//        aidEntity.setLatitude(aidDto.getLatitude());
//        aidEntity.setLongitude(aidDto.getLongitude());
//        AidCategoryEntity aidCategory;
//        aidEntity.setCreatedDate(aidDto.getCreatedDate());
//        switch (aidDto.getSelectedCategoryAid()) {
//            case 1 -> {
//                aidCategory = aidCategoryRepository.findByName("Basic Necessities Aid");
//                aidEntity.setCategoryAidEntity(aidCategory);
//            }
//            case 2 -> {
//                aidCategory = aidCategoryRepository.findByName("Healthcare Aid");
//                aidEntity.setCategoryAidEntity(aidCategory);
//            }
//            case 3 -> {
//                aidCategory = aidCategoryRepository.findByName("Education Aid");
//                aidEntity.setCategoryAidEntity(aidCategory);
//            }
//            case 4 -> {
//                aidCategory = aidCategoryRepository.findByName("Employment Aid");
//                aidEntity.setCategoryAidEntity(aidCategory);
//            }
//            case 5 -> {
//                aidCategory = aidCategoryRepository.findByName("Legal Aid");
//                aidEntity.setCategoryAidEntity(aidCategory);
//            }
//            case 6 -> {
//                aidCategory = aidCategoryRepository.findByName("Community Aid");
//                aidEntity.setCategoryAidEntity(aidCategory);
//            }
//        }
//        System.out.println(aidEntity.getId());
////        aidEntity.setCreatedDate(aidDto.getCreatedDate());
//        System.out.println("removing from service->repository");
//        System.out.println(aidEntity);
//        aidRepository.deleteByDescriptionAndLatitudeAndLongitude(description, latitude, longitude);

        aidRepository.deleteUsersAidsById(usersAidsId);
        aidRepository.deleteById(aidId);
    }

    @Override
    public AidEntity findByCategoryName(String name) {
        return null;
    }
}
