package com.w4kened.RefHelper.service.impl;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.*;
import com.w4kened.RefHelper.repository.AidCategoryRepository;
import com.w4kened.RefHelper.repository.AidRepository;
import com.w4kened.RefHelper.repository.UserRepository;
import com.w4kened.RefHelper.repository.UsersAidsRepository;
import com.w4kened.RefHelper.security.SecurityUtil;
import com.w4kened.RefHelper.service.AidService;
import com.w4kened.RefHelper.service.UserService;
import com.w4kened.RefHelper.service.UsersAidsService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    UsersAidsService usersAidsService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AidServiceImpl(AidRepository aidRepository,
                          AidCategoryRepository aidCategoryRepository,
                          UserService userService,
                          UsersAidsRepository usersAidsRepository,
                          UsersAidsService usersAidsService) {
        this.userService = userService;
        this.aidRepository = aidRepository;
        this.aidCategoryRepository = aidCategoryRepository;
        this.usersAidsRepository = usersAidsRepository;
        this.usersAidsService = usersAidsService;
    }

    public static LocalDateTime getCurrentTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTimeString = now.format(formatter);
        return LocalDateTime.parse(formattedDateTimeString, formatter);
    }


    @Override
    public List<AidEntity> findAll() {
//        save_csv();
        return aidRepository.findAll();
    }

    @Override
    public List<UsersAidsEntity> findRequestedAidsByAidIds(List<Long> aidIds) throws NotFoundException {

        return usersAidsRepository.findRequestedAidsByAidIds(aidIds);
    }

//    @Override
//    public List<UsersAidsEntity> findsResponsedAidsByAidIds(List<Long> id) throws NotFoundException {
//        return null;
//    }
//
//    @Override
//    public List<UsersAidsEntity> findsResponsedAidsByAidIds(Long userId) throws NotFoundException {
//        return usersAidsRepository.findResponsesByUserId(userId);

//    }

    public AidCategoryEntity convertAndFindCategory(AidDto aidDto) {
        if (aidDto.getSelectedCategoryAid() == 1L) {
            return aidCategoryRepository.findByName("Basic Necessities Aid");
        } else if (aidDto.getSelectedCategoryAid() == 2L) {
            return aidCategoryRepository.findByName("Healthcare Aid");

        } else if (aidDto.getSelectedCategoryAid() == 3L) {
            return aidCategoryRepository.findByName("Education Aid");

        } else if (aidDto.getSelectedCategoryAid() == 4L) {
            return aidCategoryRepository.findByName("Employment Aid");

        } else if (aidDto.getSelectedCategoryAid() == 5L) {
            return aidCategoryRepository.findByName("Legal Aid");

        } else if (aidDto.getSelectedCategoryAid() == 6L) {
            return aidCategoryRepository.findByName("Community Aid");
        }
        return null;
    }


    //TODO created_date new field on interaction table (create)
    @Override
    public void saveAid(AidDto aidDto) {
        AidEntity aidEntity = new AidEntity();
        aidEntity.setDescription(aidDto.getDescription());
        aidEntity.setAddress(aidDto.getAddress());
        aidEntity.setLatitude(aidDto.getLatitude());
        aidEntity.setLongitude(aidDto.getLongitude());
////        aidEntity.setCreatedDate(aidDto.getCreatedDate());
        LocalDateTime now = getCurrentTimeStamp();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//        String formattedDateTimeString = now.format(formatter);
//        LocalDateTime formattedDateTime = LocalDateTime.parse(formattedDateTimeString, formatter);
        aidEntity.setCreatedDate(now);

//        System.out.println("coordinates " + aidEntity.getLatitude() + aidEntity.getLongitude());

        AidCategoryEntity aidCategory = convertAndFindCategory(aidDto);
        aidEntity.setAidCategoryEntity(aidCategory);
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
        usersAidsEntity.setCreatedDate(now);
        usersAidsRepository.save(usersAidsEntity);
    }

    //TODO created_date new field on interaction table (update)
    @Override
    @Transactional
    public void updateAid(AidDto aidDto, Long id) throws NotFoundException{


        Optional<AidEntity> optionalAidEntity = aidRepository.findById(id);

        if (optionalAidEntity.isPresent()) {
            AidEntity existingAidEntity = optionalAidEntity.get();

            // Update the attributes of the existing entity with values from the updated DTO
            existingAidEntity.setDescription(aidDto.getDescription());
            existingAidEntity.setLatitude(aidDto.getLatitude());
            existingAidEntity.setLongitude(aidDto.getLongitude());
            existingAidEntity.setAddress(aidDto.getAddress());

            // Assuming you have a method to convert and set the category in the entity
            // existingAidEntity.setAidCategoryEntity(convertAndFindCategory(updatedAidDto));

            // Save the updated entity back to the repository
            aidRepository.save(existingAidEntity);

            UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
            UsersAidsEntity usersAidsEntity  = new UsersAidsEntity();
            usersAidsEntity.setAidEntity(existingAidEntity);
            usersAidsEntity.setUserEntity(userEntity);
            usersAidsEntity.setAidInteraction(AidInteraction.MODIFYING);
//            LocalDateTime now = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//            String formattedDateTimeString = now.format(formatter);
//            LocalDateTime formattedDateTime = LocalDateTime.parse(formattedDateTimeString, formatter);
//            aidEntity.setCreatedDate(formattedDateTime);

            usersAidsEntity.setCreatedDate(getCurrentTimeStamp());
            usersAidsRepository.save(usersAidsEntity);
            System.out.println("update aid calling ");

        }
         else {
            throw new NotFoundException("AidEntity with ID " + id + " not found");
        }
    }

//    @Override
//    public void removeAid(Long usersAidsId, Long aidId) {
//        usersAidsRepository.removeUsersAidsById(usersAidsId);
//        aidRepository.deleteById(aidId);
//    }

    @Override
    public void deleteAidById(Long id) throws NotFoundException {
        Optional<AidEntity> optionalAidEntity = aidRepository.findById(id);
        if (optionalAidEntity.isPresent()) {
            usersAidsRepository.deleteByAidId(id);
            aidRepository.deleteById(id); // Delete the aid by its ID
        } else {
            throw new NotFoundException("AidEntity with ID " + id + " not found");
        }
    }


    @Override
    public AidEntity findByCategoryName(String name) {
        return null;
    }

    @Override
    public AidEntity findByAidId(Long aidId) {
        return aidRepository.findByAidId(aidId);
    }

    //TODO repository changed
    @Override
    public List<AidEntity> findByCreatorUserId(Long userId) {
        return aidRepository.findByCreatorUserId(userId);
    }
//    @Override
//    public Page<AidEntity> findByCreatorUserId(Long userId, Pageable pageable) {
//        return aidRepository.findByCreatorUserId(userId, pageable);
//    }



    @Override
    public void requestAid(Long id) throws NotFoundException {
        Optional<AidEntity> optionalAidEntity = aidRepository.findById(id);

        if (optionalAidEntity.isPresent()) {
            //aid founded
            AidEntity existingAidEntity = optionalAidEntity.get();
            //geting user entity
            UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
            //creating interaction?
            UsersAidsEntity usersAidsEntity  = new UsersAidsEntity();
//
//            if (usersAidsService.findByAid())
//            if (usersAidsService.findByUserIdAndAidId(userEntity.getId(), 25L).size() > 0) {
//                return;
//
//            }
//            if (usersAidsService.findByUserId(userEntity.getId());

//            if (usersAidsService.findAll())
            usersAidsEntity.setAidEntity(existingAidEntity);
            usersAidsEntity.setUserEntity(userEntity);
            usersAidsEntity.setAidInteraction(AidInteraction.REQUESTING);
            usersAidsEntity.setCreatedDate(getCurrentTimeStamp());
            usersAidsRepository.save(usersAidsEntity);
            System.out.println("request aid calling ");
        }
        else {
            throw new NotFoundException("AidEntity with ID " + id + " not found");
        }
    }


    //TODO created_date new field on interaction table (accept)
    @Override
    public void acceptAidRequest(Long aidId, Long userId) throws NotFoundException {
        Optional<AidEntity> optionalAidEntity = aidRepository.findById(aidId);

        if (optionalAidEntity.isPresent()) {
            //aid founded
            AidEntity existingAidEntity = optionalAidEntity.get();
            //geting user entity
            Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
            if (optionalUserEntity.isPresent()) {
                          //creating interaction?
                UsersAidsEntity usersAidsEntity = new UsersAidsEntity();
                UserEntity existingUserEntity = optionalUserEntity.get();

    //
    //            if (usersAidsService.findByAid())
    //            if (usersAidsService.findByUserIdAndAidId(userEntity.getId(), 25L).size() > 0) {
    //                return;
    //
    //            }
    //            if (usersAidsService.findByUserId(userEntity.getId());

    //            if (usersAidsService.findAll())
                usersAidsEntity.setAidEntity(existingAidEntity);
                usersAidsEntity.setUserEntity(existingUserEntity);
                usersAidsEntity.setAidInteraction(AidInteraction.ACCEPTANCE);
                usersAidsEntity.setCreatedDate(getCurrentTimeStamp());
                usersAidsRepository.save(usersAidsEntity);
                System.out.println("accept aid calling ");
            }
        }
        else {
            throw new NotFoundException("user or aid ids not found");
        }
    }

    //TODO created_date new field on interaction table (reject)
    @Override
    public void rejectAidRequest(Long aidId, Long userId) throws NotFoundException {
        Optional<AidEntity> optionalAidEntity = aidRepository.findById(aidId);

        if (optionalAidEntity.isPresent()) {
            //aid founded
            AidEntity existingAidEntity = optionalAidEntity.get();
            //geting user entity
            Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
            if (optionalUserEntity.isPresent()) {
                //creating interaction?
                UsersAidsEntity usersAidsEntity = new UsersAidsEntity();
                UserEntity existingUserEntity = optionalUserEntity.get();

                //
                //            if (usersAidsService.findByAid())
                //            if (usersAidsService.findByUserIdAndAidId(userEntity.getId(), 25L).size() > 0) {
                //                return;
                //
                //            }
                //            if (usersAidsService.findByUserId(userEntity.getId());

                //            if (usersAidsService.findAll())
                usersAidsEntity.setAidEntity(existingAidEntity);
                usersAidsEntity.setUserEntity(existingUserEntity);
                usersAidsEntity.setAidInteraction(AidInteraction.REJECTION);
                usersAidsEntity.setCreatedDate(getCurrentTimeStamp());
                usersAidsRepository.save(usersAidsEntity);
                System.out.println("reject aid calling ");
            }
        }
        else {
            throw new NotFoundException("user or aid ids not found");
        }
    }


    @Override
    public Long countRequestedAidByUser(Long aidId) throws NotFoundException {
        Optional<AidEntity> optionalAidEntity = aidRepository.findById(aidId);

        if (optionalAidEntity.isPresent()) {
            //aid founded
            AidEntity existingAidEntity = optionalAidEntity.get();
            //geting user entity
            UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
            System.out.println();
            return aidRepository.countRequestedAidByAidIdAndUserId(existingAidEntity.getId(), userEntity.getId());


        }
        else {
            throw new NotFoundException("AidEntity with ID " + aidId + " not found");
        }

    }

    @Override
    public Long countAcceptedAidByUser(Long userId) throws NotFoundException {
        return null;
    }
}
