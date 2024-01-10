package com.w4kened.RefHelper.service.impl;

import com.w4kened.RefHelper.models.CityEntity;
import com.w4kened.RefHelper.models.RoleEntity;
import com.w4kened.RefHelper.repository.CityRepository;
import com.w4kened.RefHelper.repository.UserRepository;
import com.w4kened.RefHelper.repository.RoleRepository;
import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private CityRepository cityRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.cityRepository  = cityRepository;
    }

//    @Autowired UserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }



    @Override
    public boolean saveUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDto.getFirstName()+" "+userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setPhoneNumber(userDto.getPhoneNumber());
        LocalDateTime now = getCurrentTimeStamp();
        userEntity.setCreatedDate(now);
        RoleEntity roleEntity;
        switch (userDto.getSelectedRole()) {
            case 2 -> {
                roleEntity = roleRepository.findByName("ROLE_VOLUNTEER");
                userEntity.setRoleEntity(roleEntity);
            }
            case 3 -> {
                roleEntity = roleRepository.findByName("ROLE_REFUGEE");
                userEntity.setRoleEntity(roleEntity);
            }
        }
        CityEntity cityEntity = cityRepository.findByName(userDto.getCityName());
        userEntity.setCityEntity(cityEntity);
        try {
            UserEntity existingUser = userRepository.findByEmail(userDto.getEmail());
            if (existingUser != null || userDto.getPassword().length() < 8) {
                return false;
            }
            userRepository.save(userEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Map<String, Long> getRegionalDistributionOfRefugeesForChart() {
        List<Object[]> result = userRepository.getRegionalDistributionOfRefugees();
        return result
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((BigInteger) row[1]).longValue()
                ));
    }
    public static LocalDateTime getCurrentTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTimeString = now.format(formatter);
        return LocalDateTime.parse(formattedDateTimeString, formatter);
    }
}
