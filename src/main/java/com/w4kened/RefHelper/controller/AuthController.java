package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getPreviewPage() {
        return "preview";
    }



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
            System.out.println("error res " +result);
            return "authSignup";
        }

        userService.saveUser(user);
        model.addAttribute("message", "Registration successful!");
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        System.out.println("error res " +error + model);

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }


        return "authLogin";
    }
}
