package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.security.SecurityUtil;

import com.w4kened.RefHelper.service.AidService;
import com.w4kened.RefHelper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
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

//        userService.findByEmail(email).getRoleEntity();
        model.addAttribute("data", email);

        String roleName = userService.findByEmail(email).getRoleEntity().getName();
        model.addAttribute("role", roleName);

        String userFullName = userService.findByEmail(email).getName();
        model.addAttribute("userFullName", userFullName);

//        System.out.println("username = "+email);
//        System.out.println("role = "+userService.findByEmail(email).getId());

        if (Objects.equals(userService.findByEmail(email).getRoleEntity().getName(), "ROLE_VOLUNTEER")) {
            List<AidEntity> aids = new ArrayList<>();
            aids = aidService.findByCreatorUserId(userService.findByEmail(email).getId());
//            System.out.println("cout: "+aids.toArray().length);
            model.addAttribute("aidsOfferedCount", aids.toArray().length);
            model.addAttribute("aidsList", aids);
            model.addAttribute("layout", "layout");

        } else {
            model.addAttribute("layout", "refLayout");
        }

        return "home";
    }

    @GetMapping("/addAid")
    public String showAddAidForm(Model model) {
        model.addAttribute("aidDto", new AidDto());
        // Add additional attributes needed for the form (e.g., aid categories)
        // Example: model.addAttribute("aidCategories", aidService.getAllAidCategories());
        return "addAidForm"; // Assuming addAidForm.html is the Thymeleaf template
    }

    @PostMapping("/addAid")
    public String addAid(@ModelAttribute("aidDto") AidDto aidDto) {
        // Here, you would save the new aid using the AidService
        aidService.saveAid(aidDto);
        System.out.println(aidDto);
        return "redirect:/home"; // Redirect to the aids page after adding the aid
    }
}
