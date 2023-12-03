package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.security.SecurityUtil;

import com.w4kened.RefHelper.service.AidService;
import com.w4kened.RefHelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class HomeController {

    public UserService userService;
    public AidService aidService;

    @Autowired
    public HomeController(UserService userService, AidService aidService) {
        this.userService = userService;
        this.aidService = aidService;
    }


    @GetMapping("/home")
    public String getHomePage(Model model) {
        UserEntity user = new UserEntity();

        String email = SecurityUtil.getSessionUser();

        userService.findByEmail(email).getRoleEntity();
        model.addAttribute("data", email);

        String roleName = userService.findByEmail(email).getRoleEntity().getName();
        model.addAttribute("role", roleName);

        System.out.println("username = "+email);
        System.out.println("role = "+userService.findByEmail(email).getId());

        if (Objects.equals(userService.findByEmail(email).getRoleEntity().getName(), "ROLE_VOLUNTEER")) {
            model.addAttribute("layout", "layout");
        } else {
            model.addAttribute("layout", "refLayout");
        }

        return "home";
    }
}
