package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.RoleEntity;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.repository.AidCategoryRepository;
import com.w4kened.RefHelper.repository.AidRepository;
import com.w4kened.RefHelper.repository.UsersAidsRepository;
import com.w4kened.RefHelper.service.UserService;
import com.w4kened.RefHelper.service.UsersAidsService;
import com.w4kened.RefHelper.service.impl.AidServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AidServiceTest {
    @Mock
    private AidRepository aidRepository;
    @Mock
    private AidCategoryRepository aidCategoryRepository;
    @Mock
    private UserService userService;
    @Mock
    private UsersAidsRepository usersAidsRepository;
    @Mock
    private UsersAidsService usersAidsService;
    @InjectMocks
    private AidServiceImpl aidService;






//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getName()).thenReturn("test@example.com");
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(1L);
//        userEntity.setEmail("test@example.com");
//        userEntity.setRoleEntity(new RoleEntity("ROLE_VOLUNTEER", 2L));
//        when(userService.findByEmail("test@example.com")).thenReturn(userEntity);
//    }

    @Test
    void shouldToSaveAidOfferByVolunteer() throws NotFoundException {
        AidDto aidDto = new AidDto();
        aidDto.setId(5L);
        aidDto.setDescription("Test Description");
        aidDto.setAddress("Test Address");
        aidDto.setLatitude(123.45);
        aidDto.setLongitude(67.89);
        aidDto.setSelectedCategoryAid(1L);

        boolean result = aidService.saveAid(aidDto);

        assertTrue(result);
    }

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getName()).thenReturn("test@example.com");
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(1L);
//        userEntity.setEmail("test@example.com");
//        userEntity.setRoleEntity(new RoleEntity("ROLE_REFUGEE", 2L));
//        when(userService.findByEmail("test@example.com")).thenReturn(userEntity);
//    }

    @Test
    void shouldToPreventSavingAidOfferByRefugee() throws NotFoundException {
        AidDto aidDto = new AidDto();
        aidDto.setId(5L);
        aidDto.setDescription("Test Description");
        aidDto.setAddress("Test Address");
        aidDto.setLatitude(123.45);
        aidDto.setLongitude(67.89);
        aidDto.setSelectedCategoryAid(1L);

        boolean result = aidService.saveAid(aidDto);

        assertFalse(result);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setRoleEntity(new RoleEntity("ROLE_VOLUNTEER", 2L));
        when(userService.findByEmail("test@example.com")).thenReturn(userEntity);

        AidDto aidDto = new AidDto();
        aidDto.setId(5L);
        aidDto.setDescription("Test Description");
        aidDto.setAddress("Test Address");
        aidDto.setLatitude(123.45);
        aidDto.setLongitude(67.89);
        aidDto.setSelectedCategoryAid(1L);

        AidEntity mockedAidEntity = new AidEntity();
        mockedAidEntity.setId(5L);
        when(aidRepository.findById(5L)).thenReturn(Optional.of(mockedAidEntity));

        aidService.saveAid(aidDto);
    }

    @Test
    void shouldToPreventUpdatingAidOfferByNotCreator() throws NotFoundException {
        UserEntity anotherUserEntity = new UserEntity();
        anotherUserEntity.setId(2L);
        anotherUserEntity.setEmail("test2@example.com");
        anotherUserEntity.setRoleEntity(new RoleEntity("ROLE_VOLUNTEER",2L));

        AidDto existingAidDto = new AidDto();
        existingAidDto.setId(5L);
        existingAidDto.setDescription("Changed Description");
        existingAidDto.setAddress("Changed Address");
        boolean isUpdated = aidService.updateAid(existingAidDto, 5L);

        assertFalse(isUpdated);
    }

    @Test
    void shouldToPreventDeletingAidOfferByNotCreator() throws NotFoundException {
        UserEntity anotherUserEntity = new UserEntity();
        anotherUserEntity.setId(2L);
        anotherUserEntity.setEmail("test2@example.com");
        anotherUserEntity.setRoleEntity(new RoleEntity("ROLE_VOLUNTEER",2L));

        AidDto existingAidDto = new AidDto();
        existingAidDto.setId(5L);
        existingAidDto.setDescription("Changed Description");
        existingAidDto.setAddress("Changed Address");
        boolean isUpdated = aidService.deleteAidById(5L);

        assertFalse(isUpdated);
    }
}
