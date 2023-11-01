package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.security.SecurityUtil;

import com.w4kened.RefHelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    public UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/home")
    public String getHomePage(Model model) {
        UserEntity user = new UserEntity();
//        List<>
        String username = SecurityUtil.getSessionUser();

        System.out.println("username = "+username);
        return "layout";
    }
}
