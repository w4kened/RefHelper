package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getPreviewPage() {
        return "preview";
    }

//    @GetMapping("/list")
//    public String showCityList(Model model) {
//        // Simulated list of cities (replace this with your database retrieval logic)
//
//        return "cityList"; // Thymeleaf template name
//    }
    @RequestMapping("/register")
    public String getRegisterPage(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);

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

        model.addAttribute("cities", cities);
        return "authSignup";
    }

//    @GetMapping("/city")
//    public String showCityForm(Model model) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://raw.githubusercontent.com/sk1me/polish-town-string/main/polish-town-string.txt";
//        String cities = restTemplate.getForObject(url, String.class);
//        System.out.println(cities);
//        List<String> cityList = Arrays.asList(cities.split(","));
//        model.addAttribute("cities", cityList);
//        return "city/cityForm";
//    }
//    @GetMapping("/city")
//    public String showCityForm(Model model, @RequestParam(defaultValue = "0") int page) {
//        List<String> cities = getCitiesFromApi(page);
//        model.addAttribute("cities", cities);
//        return "city/cityForm";
//    }

//    @GetMapping("/cities")
//    public String getCities(Model model) {
//        System.out.println("asdasdas");
//
//        RestTemplate restTemplate = new RestTemplate();
//        String apiUrl = "http://api.geonames.org/searchJSON?country=PL&maxRows=20&username=refhelperapplication";
//        // Replace YOUR_USERNAME with your GeoNames username
//
//        // Fetch city data from API
//        String citiesJson = restTemplate.getForObject(apiUrl, String.class);
//
//        System.out.println(citiesJson);
////
//        model.addAttribute("citiesJson", citiesJson);
//        return "cityList"; // Thymeleaf template name
//    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") UserDto user,
                           BindingResult result, Model model) {
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            result.rejectValue("repeatPassword", "error.user", "Passwords do not match");
            return "redirect:/register";
            // passwords match, continue with registration
        }
        UserEntity existingUserEmail = userService.findByEmail(user.getEmail());
        if(existingUserEmail != null && existingUserEmail.getEmail() != null &&
                !existingUserEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }
        if(result.hasErrors()) {
            model.addAttribute("user", user);
            return "authSignup";
        }

        userService.saveUser(user);
        model.addAttribute("message", "Registration successful!");
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "authLogin";
    }
//    @PostMapping("/login")
//    public String login(@RequestParam String email, @RequestParam String password, Model model) {
//        if (authenticationService.authenticateUser(email, password)) {
//            // Authentication successful
//            return "redirect:/home"; // or any other success page
//        } else {
//            // Authentication failed
//            model.addAttribute("error", "Invalid username or password");
//            return "authLogin";
//        }
//    }
}
