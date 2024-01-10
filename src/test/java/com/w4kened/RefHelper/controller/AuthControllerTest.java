package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.repository.CityRepository;
import com.w4kened.RefHelper.repository.RegionRepository;
import com.w4kened.RefHelper.repository.UserRepository;
import com.w4kened.RefHelper.security.CustomUserDetailsService;
import com.w4kened.RefHelper.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    public static PasswordEncoder passwordEncoder;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    @InjectMocks
    private AuthController controller;

//
//
//    @BeforeEach
//    void setUp() {
//        when(passwordEncoder.encode("qwerty123123")).thenReturn("encodedPassword");
//
//
//        RegionEntity existingRegion = RegionEntity.builder()
//                .name("Masovian").build();
//        CityEntity existingCity = CityEntity.builder()
//                .name("Warszawa")
//                .regionEntity(existingRegion).build();
//        RoleEntity existingRole = RoleEntity.builder()
//                .name("ROLE_VOLUNTEER").build();
//        UserEntity existingUser = UserEntity.builder()
//                    .email("existingEmail@gmail.com")
//                    .password("qwerty123123")
//                    .name("Jan Kowalski")
//                    .password(passwordEncoder.encode("qwerty123123"))
//                    .phoneNumber("511211232")
//                    .cityEntity(existingCity)
//                    .roleEntity(existingRole).build();
//
//
//        when(userRepository.findByEmail("existingEmail@gmail.com")).thenReturn(existingUser);
//
//        UserDetails userDetails = new User(
//                existingUser.getEmail(),
//                existingUser.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority(existingUser.getRoleEntity().getName()))
//        );
//
//        when(customUserDetailsService.loadUserByUsername("existingEmail@gmail.com")).thenReturn(userDetails);
//    }

    @Test
    public void shouldLoginSuccessfully() throws Exception {
        mockMvc.perform(post("/login")
                        .param("email", "existingEmail@gmail.com")
                        .param("password", "qwerty123123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    public void shouldFailToLoginWithWrongCredentials() throws Exception {
        mockMvc.perform(post("/login")
                        .param("email", "wrongEmail@gmail.com")
                        .param("password", "wrongPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }





    @Test
    public void shouldPreventToRegisterUserWithExistingEmail() throws Exception {
            UserDto userDto = new UserDto();
            userDto.setFirstName("Piotr");
            userDto.setLastName("Grzyb");
            userDto.setEmail("existingEmail@gmail.com");
            userDto.setPassword("password123");
            userDto.setRepeatPassword("password123");
            userDto.setCityName("Kielce");
            userDto.setPhoneNumber("534123444");

            mockMvc.perform(post("/register/save")
                            .param("cityName", userDto.getCityName())
                            .param("phoneNumber", userDto.getPhoneNumber())
                            .param("firstName", userDto.getFirstName())
                            .param("lastName", userDto.getLastName())
                            .param("email", userDto.getEmail())
                            .param("password", userDto.getPassword())
                            .param("repeatPassword", userDto.getRepeatPassword()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/register?fail"));
    }

    @Test
    public void shouldVerifyPasswordEncryption() throws Exception {
        UserEntity userEntity = userService.findByEmail("existingEmail@gmail.com");
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        boolean matches = encoder.matches("qwerty123123", userEntity.getPassword());

        assertTrue(matches);
    }

    @Test
    public void testGetRegisterPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("authSignup"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user", "cities"));
    }
//
//    @Test
//    public void shouldPreventToRegisterUserWithExistingEmail() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setFirstName("Szymon");
//        userDto.setLastName("Grzyb");
//        userDto.setEmail("existingEmail@gmail.com");
//        userDto.setPassword("password123123");
//        userDto.setRepeatPassword("password123123");
//        userDto.setCityName("Kielce");
//        userDto.setPhoneNumber("534123444");
//
////        UserEntity existingUser = new UserEntity();
////        existingUser.setEmail("existingEmail@test.com");
////
////        // Mock the UserService
////        UserService userService = mock(UserService.class);
////        when(userService.findByEmail(existingUser.getEmail())).thenReturn(existingUser);
////
////        // Create a new instance of AuthController with the mocked UserService
////        AuthController authController = new AuthController(userService);
//
//        // Perform the request and assert the response
//        mockMvc.perform(post("/register/save")
//                        .param("cityName", userDto.getCityName())
//                        .param("phoneNumber", userDto.getPhoneNumber())
//                        .param("firstName", userDto.getFirstName())
//                        .param("lastName", userDto.getLastName())
//                        .param("email", userDto.getEmail())
//                        .param("password", userDto.getPassword())
//                        .param("repeatPassword", userDto.getRepeatPassword()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/register?fail"));
//    }

//
//
//    @Test
//    void testGetRegisterPage() {
//        Model model = mock(Model.class);
//        String result = controller.getRegisterPage(model);
//        assertEquals("authSignup", result);
//
//        UserDto userDto = new UserDto();
//        assertNotNull(model.getAttribute("user"));
//        assertEquals(userDto, model.getAttribute("user"));
//
//
//        List<String> cities = new ArrayList<>();
//        cities.add("Rzeszów");
//        cities.add("Lublin");
//        cities.add("Kraków");
//        cities.add("Kielce");
//        cities.add("Warszawa");
//        cities.add("Białystok");
//        cities.add("Katowice");
//        cities.add("Łódź");
//        cities.add("Bydgoszcz");
//        cities.add("Olsztyn");
//        cities.add("Gdańsk");
//        cities.add("Poznań");
//        cities.add("Wrocław");
//        cities.add("Gorzów Wielkopolski");
//
//
//        assertNotNull(model.getAttribute("cities"));
//        assertEquals(cities, model.getAttribute("cities"));
//    }

    @Test
    void testRegister_ValidUser_RedirectToSuccessPage() {
        UserDto userDto = new UserDto();
        userDto.setEmail("johndoe@example.com");
        userDto.setPassword("password");
        userDto.setRepeatPassword("password");

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        String resultRedirect = controller.register(userDto, result, mock(Model.class));
        assertEquals("redirect:/register?success", resultRedirect);
    }

    @Test
    void testRegister_PasswordsDoNotMatch_AddErrorAndRedirectToRegisterPage() {
        UserDto userDto = new UserDto();
        userDto.setEmail("johndoe@example.com");
        userDto.setPassword("password1");
        userDto.setRepeatPassword("password2");

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        List<ObjectError> expectedErrors = new ArrayList<>();
        expectedErrors.add(new ObjectError("repeatPassword", "Passwords do not match"));

        when(result.getAllErrors()).thenReturn(expectedErrors);

        String resultRedirect = controller.register(userDto, result, mock(Model.class));
        assertEquals("redirect:/register", resultRedirect);
    }
}
