package com.w4kened.RefHelper.service.impl;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.models.*;
import com.w4kened.RefHelper.repository.*;
import com.w4kened.RefHelper.security.SecurityUtil;
import com.w4kened.RefHelper.service.AidService;
import com.w4kened.RefHelper.service.UserService;
import com.w4kened.RefHelper.service.UsersAidsService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private RegionRepository regionRepository;

    @Autowired
    public AidServiceImpl(AidRepository aidRepository,
                          AidCategoryRepository aidCategoryRepository,
                          UserService userService,
                          UsersAidsRepository usersAidsRepository,
                          UsersAidsService usersAidsService,
                          RegionRepository regionRepository) {
        this.userService = userService;
        this.aidRepository = aidRepository;
        this.aidCategoryRepository = aidCategoryRepository;
        this.usersAidsRepository = usersAidsRepository;
        this.usersAidsService = usersAidsService;
        this.regionRepository = regionRepository;
    }



    public static LocalDateTime getCurrentTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTimeString = now.format(formatter);
        return LocalDateTime.parse(formattedDateTimeString, formatter);
    }

    public List<RegionEntity> getRegions() {
        return (List<RegionEntity>) regionRepository.findAll();
    }

    @Override
    public List<AidEntity> findAll() {
        return aidRepository.findAll();
    }

    @Override
    public List<UsersAidsEntity> findRequestedAidsByAidIds(List<Long> aidIds) throws NotFoundException {
        return usersAidsRepository.findRequestedAidsByAidIds(aidIds);
    }

    public AidCategoryEntity convertAndFindCategory(AidDto aidDto) {
        Map<Long, String> categoryMap = new HashMap<>();
        categoryMap.put(1L, "Basic Necessities Aid");
        categoryMap.put(2L, "Healthcare Aid");
        categoryMap.put(3L, "Education Aid");
        categoryMap.put(4L, "Employment Aid");
        categoryMap.put(5L, "Legal Aid");
        categoryMap.put(6L, "Community Aid");

        Long selectedCategoryAid = aidDto.getSelectedCategoryAid();
        String categoryName = categoryMap.get(selectedCategoryAid);
        if (categoryName != null) {
            return aidCategoryRepository.findByName(categoryName);
        }
        return null;
    }


    @Override
    public boolean saveAid(AidDto aidDto) {
        System.out.println("true");
        UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
        if (!userEntity.getRoleEntity().getName().equals("ROLE_VOLUNTEER")) {
            return false;
        }
        AidCategoryEntity aidCategory = convertAndFindCategory(aidDto);

        AidEntity aidEntity = AidEntity.builder()
                .description(aidDto.getDescription())
                .address(aidDto.getAddress())
                .latitude(aidDto.getLatitude())
                .longitude(aidDto.getLongitude())
                .createdDate(getCurrentTimeStamp())
                .aidCategoryEntity(aidCategory)
                .build();
        aidRepository.save(aidEntity);


        UsersAidsEntity usersAidsEntity  = UsersAidsEntity.builder()
                .aidEntity(aidEntity)
                .userEntity(userEntity)
                .aidInteraction(AidInteraction.CREATING)
                .createdDate(getCurrentTimeStamp())
                .build();
        usersAidsRepository.save(usersAidsEntity);

        return true;
    }

    @Override
    @Transactional
    public boolean updateAid(AidDto aidDto, Long id) throws NotFoundException{
        UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
        Optional<AidEntity> optionalAidEntity = aidRepository.findById(id);
        if (optionalAidEntity.isPresent()) {
            AidEntity existingAidEntity = optionalAidEntity.get();
            if (usersAidsRepository.isCreatorOfAid(id, userEntity.getId()) < 1) {
                return false;
            }

            existingAidEntity = AidEntity.builder()
                    .description(aidDto.getDescription())
                    .latitude(aidDto.getLatitude())
                    .longitude(aidDto.getLongitude())
                    .address(aidDto.getAddress())
                    .build();
            aidRepository.save(existingAidEntity);


            UsersAidsEntity usersAidsEntity  = UsersAidsEntity.builder()
                    .aidEntity(existingAidEntity)
                    .userEntity(userEntity)
                    .aidInteraction(AidInteraction.MODIFYING)
                    .createdDate(getCurrentTimeStamp())
                    .build();
            usersAidsRepository.save(usersAidsEntity);

            return true;
        }
        else {
            throw new NotFoundException("AidEntity with ID " + id + " not found");
        }
    }


    @Override
    public boolean deleteAidById(Long id) throws NotFoundException {
        Optional<AidEntity> optionalAidEntity = aidRepository.findById(id);
        if (optionalAidEntity.isPresent()) {
            UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
            if (usersAidsRepository.isCreatorOfAid(id, userEntity.getId()) < 1) {
                return false;
            }
            usersAidsRepository.deleteByAidId(id);
            aidRepository.deleteById(id);
            return true;
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

    @Override
    public List<AidEntity> findByCreatorUserId(Long userId) {
        return aidRepository.findByCreatorUserId(userId);
    }

    @Override
    public List<AidEntity> findByRequesterUserId(Long userId) {
        return aidRepository.findByRequesterUserId(userId);
    }

    @Override
    public boolean requestAid(Long id) throws NotFoundException {
        Optional<AidEntity> optionalAidEntity = aidRepository.findById(id);
        if (optionalAidEntity.isPresent()) {
            AidEntity existingAidEntity = optionalAidEntity.get();
            UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
            UsersAidsEntity usersAidsEntity  = new UsersAidsEntity();
            if (!userEntity.getRoleEntity().getName().equals("ROLE_REFUGEE")) {
                return false;
            }
            usersAidsEntity = UsersAidsEntity.builder()
                    .aidEntity(existingAidEntity)
                    .userEntity(userEntity)
                    .aidInteraction(AidInteraction.REQUESTING)
                    .createdDate(getCurrentTimeStamp())
                    .build();
            usersAidsRepository.save(usersAidsEntity);

            return true;
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
            AidEntity existingAidEntity = optionalAidEntity.get();
            Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
            if (optionalUserEntity.isPresent()) {

                UsersAidsEntity usersAidsEntity = new UsersAidsEntity();
                UserEntity existingUserEntity = optionalUserEntity.get();
                usersAidsEntity = UsersAidsEntity.builder()
                        .aidEntity(existingAidEntity)
                        .aidInteraction(AidInteraction.ACCEPTANCE)
                        .createdDate(getCurrentTimeStamp())
                        .build();

                usersAidsRepository.save(usersAidsEntity);
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
            AidEntity existingAidEntity = optionalAidEntity.get();
            Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
            if (optionalUserEntity.isPresent()) {
                UsersAidsEntity usersAidsEntity = new UsersAidsEntity();
                UserEntity existingUserEntity = optionalUserEntity.get();
                usersAidsEntity = UsersAidsEntity.builder()
                        .aidEntity(existingAidEntity)
                        .userEntity(existingUserEntity)
                        .aidInteraction(AidInteraction.REJECTION)
                        .createdDate(getCurrentTimeStamp())
                        .build();
                usersAidsRepository.save(usersAidsEntity);
            }
        }
        else {
            throw new NotFoundException("user or aid ids not found");
        }
    }

    @Override
    public Map<String, Long> getRegionalDistributionOfAidsForChart() {
        List<AidEntity> aids = aidRepository.findAll();
        List<RegionEntity> regions = getRegions();
        Map<String, Long> mapOfAidsRegionalDistribution  = new HashMap<>();
        for (RegionEntity regionEntity : regions) {
            mapOfAidsRegionalDistribution.put(regionEntity.getName(), 0L);
        }
        for (AidEntity aidEntity : aids) {
            for (String region : mapOfAidsRegionalDistribution.keySet()) {
                if (aidEntity.getAddress().contains(region)) {
                    mapOfAidsRegionalDistribution.compute(region, (k, v) -> v + 1);
                }
            }
        }
        return mapOfAidsRegionalDistribution;
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
