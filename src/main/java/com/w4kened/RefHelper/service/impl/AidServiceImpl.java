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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

//    @Autowired
//    public AidServiceImpl(AidRepository aidRepository
//                          ) {
//        this.aidRepository = aidRepository;
//    }



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
//        save_csv();
        return aidRepository.findAll();
    }

    @Override
    public List<UsersAidsEntity> findRequestedAidsByAidIds(List<Long> aidIds) throws NotFoundException {
        return usersAidsRepository.findRequestedAidsByAidIds(aidIds);
    }

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


    @Override
    public boolean saveAid(AidDto aidDto) {
        System.out.println("true");
        UserEntity userEntity = userService.findByEmail(SecurityUtil.getSessionUser());
        if (!userEntity.getRoleEntity().getName().equals("ROLE_VOLUNTEER")) {
            return false;
        }
        AidEntity aidEntity = new AidEntity();
        aidEntity.setDescription(aidDto.getDescription());
        aidEntity.setAddress(aidDto.getAddress());
        aidEntity.setLatitude(aidDto.getLatitude());
        aidEntity.setLongitude(aidDto.getLongitude());
        LocalDateTime now = getCurrentTimeStamp();
        aidEntity.setCreatedDate(now);
        AidCategoryEntity aidCategory = convertAndFindCategory(aidDto);
        aidEntity.setAidCategoryEntity(aidCategory);
        aidRepository.save(aidEntity);
        UsersAidsEntity usersAidsEntity  = new UsersAidsEntity();
        usersAidsEntity.setAidEntity(aidEntity);
        usersAidsEntity.setUserEntity(userEntity);
        usersAidsEntity.setAidInteraction(AidInteraction.CREATING);
        usersAidsEntity.setCreatedDate(now);
        usersAidsRepository.save(usersAidsEntity);
        System.out.println("true");
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

            existingAidEntity.setDescription(aidDto.getDescription());
            existingAidEntity.setLatitude(aidDto.getLatitude());
            existingAidEntity.setLongitude(aidDto.getLongitude());
            existingAidEntity.setAddress(aidDto.getAddress());
            aidRepository.save(existingAidEntity);
            UsersAidsEntity usersAidsEntity  = new UsersAidsEntity();
            usersAidsEntity.setAidEntity(existingAidEntity);
            usersAidsEntity.setUserEntity(userEntity);
            usersAidsEntity.setAidInteraction(AidInteraction.MODIFYING);
            usersAidsEntity.setCreatedDate(getCurrentTimeStamp());
            usersAidsRepository.save(usersAidsEntity);
            return true;
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
//    @Override
//    public Page<AidEntity> findByCreatorUserId(Long userId, Pageable pageable) {
//        return aidRepository.findByCreatorUserId(userId, pageable);
//    }



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
            usersAidsEntity.setAidEntity(existingAidEntity);
            usersAidsEntity.setUserEntity(userEntity);
            usersAidsEntity.setAidInteraction(AidInteraction.REQUESTING);
            usersAidsEntity.setCreatedDate(getCurrentTimeStamp());
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
                usersAidsEntity.setAidEntity(existingAidEntity);
                usersAidsEntity.setUserEntity(existingUserEntity);
                usersAidsEntity.setAidInteraction(AidInteraction.ACCEPTANCE);
                usersAidsEntity.setCreatedDate(getCurrentTimeStamp());
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
                usersAidsEntity.setAidEntity(existingAidEntity);
                usersAidsEntity.setUserEntity(existingUserEntity);
                usersAidsEntity.setAidInteraction(AidInteraction.REJECTION);
                usersAidsEntity.setCreatedDate(getCurrentTimeStamp());
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
