package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.service.UserService;
import com.w4kened.RefHelper.service.UsersAidsService;
import com.w4kened.RefHelper.service.impl.AidServiceImpl;
import com.w4kened.RefHelper.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useDefaultDateFormatsOnly;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AidServiceImpl aidService;
    @Autowired
    private UsersAidsService usersAidsService;
    @Autowired
    HomeController homeController;
    List<UserDto> listOfUsers;

    //    @Test
//    public void testAllUsersInserted() {
//        List<UserDto> listOfUsers = generateMultipleUserDTOs();
//        Map<Boolean, UserDto> mapOfInserts = new HashMap<>();
//
//        for (UserDto userDto : listOfUsers) {
//            mapOfInserts.put(userService.saveUser(userDto), userDto);
//        }
//        for (Boolean key : mapOfInserts.keySet()) {
//            assertTrue(key, "Expected all keys to be true");
//        }
//    }
    @BeforeEach
    public void setUp() {
        this.listOfUsers = generateMultipleUserDTOs();
        List<Boolean> isInserted = new ArrayList<>(Collections.nCopies(200, false));
        Map<Boolean, UserDto> mapOfInserts = new HashMap<>();
        for (UserDto userDto : listOfUsers) {
            mapOfInserts.put(userService.saveUser(userDto), userDto);
        }
    }
    @Test
    public void shouldToEnterAllUsersHomePageWithWrongCredentials() throws Exception {
        int dmlCounter = 0;
        List<UserDto> listOfUsers = generateMultipleUserDTOs();
        Map<Boolean, UserDto> mapOfSelects = new HashMap<>();
        for (UserDto userDto : listOfUsers) {
            mockMvc.perform(post("/login")
                            .param("email", userDto.getEmail())
                            .param("password", userDto.getPassword()+1))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?error=true"));// Expecting redirection to /home
            dmlCounter++;
        }

        assertThat(dmlCounter).isEqualTo(200);
    }
    @Test
    public void shouldToEnterAllUsersHomePageWithMatchingCredentials() throws Exception {
        int dmlCounter = 0;
        for (UserDto userDto : listOfUsers) {
            mockMvc.perform(post("/login")
                            .param("email", userDto.getEmail())
                            .param("password", userDto.getPassword()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/home"));
            dmlCounter++;
        }
        assertThat(dmlCounter).isEqualTo(200);
    }
    @Test
    public void shouldToSaveRandomAids() throws Exception {
        List<AidDto> aidDtos = generateMultipleAidDTOs();
        int dmlCounter = 0;
        for (AidDto aidDto : aidDtos) {
            for (UserDto userDto : listOfUsers) {
                if (userDto.getSelectedRole() == 2) {
                    mockMvc.perform(post("/addAid")
                                    .with(csrf())
                                    .flashAttr("aidDto", aidDto)
                                    .with(user(userDto.getEmail())))
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/home"));
                    dmlCounter++;
                }
            }
        }
        assertThat(dmlCounter).isEqualTo(1600);
    }
    @Test
    public void shouldToRequestRandomAids() throws Exception {
        List<AidEntity> aidEntities = aidService.findAll();
        for (AidEntity aidEntity : aidEntities) {
            String getEndpoint = "/requestAid/" + aidEntity.getId();
            UserDto randomuser = listOfUsers.get((int)(Math.random() * listOfUsers.size()));
            if (randomuser.getSelectedRole() == 3) {
                mockMvc.perform(get(getEndpoint)
                                .with(csrf())
                                .with(user(randomuser.getEmail())))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/home?AidRequestedSuccessfull"));
            }
        }
    }


//
//    @Test
//    public void testAddAid() throws Exception {
//        List<AidDto> aidDtos = generateMultipleAidDTOs();
//                    mockMvc.perform(post("/addAid")
//                            .with(csrf())
//                            .flashAttr("aidDto", aidDtos.get(0))
//                            .with(user(listOfUsers.get(0).getEmail()))) // Assuming user is authenticated
//                    .andExpect(status().is3xxRedirection())
//                    .andExpect(redirectedUrl("/home"));
//
////        for (AidDto aidDto : aidDtos) {
////            mockMvc.perform(post("/addAid")
////                            .with(csrf())
////                            .flashAttr("aidDto", aidDto)
////                            .with(user("testUserVol@gmail.com"))) // Assuming user is authenticated
////                    .andExpect(status().is3xxRedirection())
////                    .andExpect(redirectedUrl("/home"));
////        }
//
////        verify(aidService, times(1)).saveAid(any(AidDto.class)); // Check if saveAid is called once with any AidDto
//    }
//
//    @Test
//    public void test() throws Exception {
//
//
//        mockMvc.perform(get("/home")
//                        .with(csrf())
//                        .with(user("testUserVol@gmail.com"))) // Assuming user is authenticated
//                .andExpect(status().isOk());
//
////        verify(aidService, times(1)).saveAid(any(AidDto.class)); // Check if saveAid is called once with any AidDto
//    }
    public static List<UserDto> generateMultipleUserDTOs() {
        List<UserDto> userDtos = new ArrayList<>();
        String volEmail = "testVolEmail";
        String volFname = "testVolFname";
        String volLname = "testVolLname";
        String refEmail = "testRefEmail";
        String refFname = "testRefFname";
        String refLname = "testRefLname";
        final String password = "test123123";

        List<String> cities = new ArrayList<>();
        cities.add("Rzeszów");
        cities.add("Lublin");
        cities.add("Kraków");
        cities.add("Kielce");
        cities.add("Warszawa");
        cities.add("Białystok");
        cities.add("Katowice");
        cities.add("Łódź");
        cities.add("Bydgoszcz");
        cities.add("Olsztyn");
        cities.add("Gdańsk");
        cities.add("Poznań");
        cities.add("Wrocław");
        cities.add("Gorzów Wielkopolski");
//        String[] names = Arrays.asList("");

        int userNumber = 0;
        for (Integer selectedRole = 2; selectedRole <= 3; selectedRole++) {
            for (Integer i = 0; i < 100; i++) {
                UserDto newtestUser;
                switch (selectedRole) {
                    case 2 -> {
                        newtestUser = UserDto.builder().firstName(volFname + i).lastName(volLname + i).email(volEmail + i).password("test123123").selectedRole(selectedRole).phoneNumber(String.format("%03d%03d%03d", (int) Math.floor(999 * Math.random()), (int) Math.floor(999 * Math.random()), (int) Math.floor(999 * Math.random()))).cityName(cities.get((int) (Math.random() * cities.size()))).build();
                        userDtos.add(newtestUser);
                        userNumber++;
                        break;
                    }
                    case 3 -> {
                        newtestUser = UserDto.builder()
                                .firstName(refFname + i)
                                .lastName(refLname + i)
                                .email(refEmail + i)
                                .password("test123123")
                                .selectedRole(selectedRole)
                                .phoneNumber(String.format("%03d%03d%03d",
                                        (int) Math.floor(999 * Math.random()),
                                        (int) Math.floor(999 * Math.random()),
                                        (int) Math.floor(999 * Math.random())))
                                .cityName(cities.get((int) (Math.random() * cities.size()))).build();
                        userDtos.add(newtestUser);
                        userNumber++;
                    }
                }
            }
        }
        return userDtos;
    }

    public static List<AidDto> generateMultipleAidDTOs() {
        List<AidDto> aids = new ArrayList<>();
        String description = "Test Description";
//        newAid.setAddress("Warszawa");
        List<String> addresses = Arrays.asList(
                "37A Garncarska; Warsaw; Masovian Voivodeship; 04-886; Poland",
                "42 Promienna; Warsaw; Masovian Voivodeship; 05-077; Poland",
                "17; Jabłonowo Pomorskie; Brodnica County; Kuyavian-Pomeranian Voivodeship; 87-330; Poland",
                "455; gmina Jeleśnia; Żywiec County; Silesian Voivodeship; 34-341; Poland",
                "64 19 Kwietnia; gmina Raszyn; Pruszków County; Masovian Voivodeship; 05-090; Poland",
                "1 Rodzinna; gmina Wiązowna; Otwock County; Masovian Voivodeship; 05-462; Poland",
                "14 Kwiatowa; gmina Michałowice; Pruszków County; Masovian Voivodeship; 05-816; Poland",
                "4 Królowej Jadwigi; gmina Ryglice; Tarnów County; Lesser Poland Voivodeship",
                "27 Fortuny; Jelonki; Warsaw; Masovian Voivodeship; 01-339; Poland",
                "81 Aleja Krakowska; Warsaw; Masovian Voivodeship; 02-180; Poland",
        "53 Stanisławowska; gmina Halinów; Mińsk County; Masovian Voivodeship; 05-079; Poland",
        "3 Franciszka Kostrzewskiego; Warsaw; Masovian Voivodeship; 00-768; Poland",
        "63 Błotna; gmina Łomianki; Warsaw West County; Masovian Voivodeship; 05-092; Poland",
        "4 Pod Lipą; Warsaw; Masovian Voivodeship; 02-798; Poland",
        "70 Generała Romana Abrahama; gmina Izabelin; Warsaw West County; Masovian Voivodeship; 05-080; Poland"
        );

        BigDecimal minLatitude = new BigDecimal("45.027710687");
        BigDecimal maxLatitude = new BigDecimal("53.752887536");
        BigDecimal minLongitude = new BigDecimal("15.187974902");
        BigDecimal maxLongitude = new BigDecimal("23.039467224");

        for (int i = 0; i < 16; i++) {
            AidDto newAid = new AidDto();
            newAid.setSelectedCategoryAid(1 + (long) (Math.random() * 5));
            newAid.setDescription(description + i);
            newAid.setAddress(addresses.get((int) (Math.random() * addresses.size())));

            BigDecimal latitudeRange = maxLatitude.subtract(minLatitude);
            BigDecimal longitudeRange = maxLongitude.subtract(minLongitude);
            BigDecimal randomLatitudeOffset = latitudeRange.multiply(new BigDecimal(Math.random()));
            BigDecimal randomLongitudeOffset = longitudeRange.multiply(new BigDecimal(Math.random()));
            BigDecimal latitude = minLatitude.add(randomLatitudeOffset).setScale(9, RoundingMode.HALF_UP);
            BigDecimal longitude = minLongitude.add(randomLongitudeOffset).setScale(9, RoundingMode.HALF_UP);
            newAid.setLatitude(latitude.doubleValue());
            newAid.setLongitude(longitude.doubleValue());
            aids.add(newAid);
        }
        return aids;
    }



}
