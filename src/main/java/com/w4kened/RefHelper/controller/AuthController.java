package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.UserDto;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @RequestMapping("/register")
    public String getRegisterPage(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "authSignup";
    }


    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") UserDto user,
                           BindingResult result, Model model) {
        System.out.println(user.getEmail());
        UserEntity existingUserEmail = userService.findByEmail(user.getEmail());
        if(existingUserEmail != null && existingUserEmail.getEmail() != null &&
                !existingUserEmail.getEmail().isEmpty()) {
//            result.rejectValue("email", null,
//                    "There is already an account registered with the same email");
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
