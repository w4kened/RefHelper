package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.repository.CityRepository;
import com.w4kened.RefHelper.repository.RoleRepository;
import com.w4kened.RefHelper.repository.UserRepository;
import com.w4kened.RefHelper.service.UserService;
import com.w4kened.RefHelper.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    CityRepository cityRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl; // Not an interface, but the concrete implementation
    @Test
    void shouldToPreventSavingUserWithExistingEmail() {
        // Mocking an existing user with the same email
        when(userRepository.findByEmail("existingEmail@gmail.com"))
                .thenReturn(new UserEntity("existingEmail@gmail.com", "password123"));

        UserDto newUser = UserDto.builder()
                        .firstName("Jan")
                        .lastName("Kowalski")
                        .email("existingEmail@gmail.com")
                        .password("123123")
                        .cityName("Warszawa")
                        .build();
        boolean isRegistered = userServiceImpl.saveUser(newUser);

        assertFalse(isRegistered); // The registration should fail
    }
    @Test
    void shouldToPreventSavingUserWithPasswordLengthLessThan8() {
        // Mocking the dependencies
        UserRepository userRepository = mock(UserRepository.class);
//        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserService userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder, cityRepository);

        // Creating a user with a password less than 8 characters
        UserDto userWithShortPassword = new UserDto();
        userWithShortPassword.setPassword("1234567"); // Password length < 8

        // Attempt to save user
        boolean isSaved = userService.saveUser(userWithShortPassword);

        // Assertion: User should not be saved due to short password
        assertFalse(isSaved);
    }
}
