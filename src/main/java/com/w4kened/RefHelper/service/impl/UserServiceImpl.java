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


    @Override
    public void saveUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDto.getFirstName()+" "+userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setPhoneNumber(userDto.getPhoneNumber());

        String currentTime = LocalDateTime
                .now()
                .format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.parse(currentTime, formatter);
        userEntity.setCreatedDate(now);

        System.out.println(now);
        System.out.println("selected "+userDto.getSelectedRole());
        RoleEntity roleEntity;
        switch (userDto.getSelectedRole()) {
            case 2 -> {
                roleEntity = roleRepository.findByName("ROLE_REFUGEE");
                userEntity.setRoleEntity(roleEntity);
            }
            case 3 -> {
                roleEntity = roleRepository.findByName("ROLE_VOLUNTEER");
                userEntity.setRoleEntity(roleEntity);
            }
        }

        CityEntity cityEntity = cityRepository.findByName(userDto.getCityName());
        userEntity.setCityEntity(cityEntity);
        userRepository.save(userEntity);
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

//    @Override
//    public RoleEntity getRoleOfUser(String email) {
//        return userRepository.findByEmail(email).getRoleEntity();
//    }
//    @Override
//    public List<UserDto> findAllUsers() {
//        List<UserEntity> users = userRepository.findAll();
//        return users.stream()
//                    .map((user) -> map)
//        return userRepository.findAll();
//    }
}
