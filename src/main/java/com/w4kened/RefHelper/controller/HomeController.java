package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.models.AidCategoryEntity;
import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.repository.AidCategoryRepository;
import com.w4kened.RefHelper.repository.AidRepository;
import com.w4kened.RefHelper.repository.UsersAidsRepository;
import com.w4kened.RefHelper.security.SecurityUtil;

import com.w4kened.RefHelper.service.AidService;
import com.w4kened.RefHelper.service.UserService;
import com.w4kened.RefHelper.service.UsersAidsService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {

    public UserService userService;
    public AidService aidService;
    public UsersAidsService usersAidsService;

    private final AidCategoryRepository aidCategoryRepository;
    private final AidRepository aidRepository;
    private final UsersAidsRepository usersAidsRepository;

    @Autowired
    public HomeController(UserService userService, AidService aidService, AidCategoryRepository aidCategoryRepository, AidRepository aidRepository, UsersAidsRepository usersAidsRepository) {
        this.userService = userService;
        this.aidService = aidService;
        this.aidCategoryRepository = aidCategoryRepository;
        this.aidRepository = aidRepository;
        this.usersAidsRepository = usersAidsRepository;
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

    @GetMapping("/editAid/{id}")
    public ModelAndView showEditAidForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        AidEntity aidEntity = aidService.findByAidId(id);
        if (aidEntity == null) {
            throw new NotFoundException("Not aid with ID " + id);
        }

        ModelAndView editView = new ModelAndView();
        editView.setViewName("editAidForm");
        AidDto aidDto = new AidDto(aidEntity);
        editView.addObject(aidDto);

        return editView; // Assuming addAidForm.html is the Thymeleaf template
    }

    @PostMapping("/editAid/{id}")
    public ModelAndView editAid(@PathVariable("id") Long id, @ModelAttribute("aidDto") AidDto aidDto) throws NotFoundException {
        ModelAndView editView = new ModelAndView();
        System.out.println("post "+aidDto);
        aidService.updateAid(aidDto, id);
        editView.addObject(aidDto);
        editView.setViewName("redirect:/home");
        return editView;
    }

    @GetMapping("/deleteAid/{id}")
    public String deleteAid(@PathVariable("id") Long id) throws NotFoundException {
        aidService.deleteAidById(id);
        return "redirect:/home";
    }
//
//    @PostMapping("/deleteAid/{id}")
//    public String editAid(@PathVariable("id") Long id) throws Exception {
//        AidEntity aidEntity = aidService.findByAidId(id);
//        if (aidEntity == null) {
//            throw new NotFoundException("Not aid with ID " + id);
//        }
//        aidService.deleteById(id);
//        return "redirect:/home";
//    }
//
//    @PostMapping("/editAid/{id}")
//    public String editForm(@PathVariable("id") Long id, Model model) {
//        AidEntity aidEntity = aidService.findByAidId(id);
//        AidDto aidDto = new AidDto(aidEntity);
//        model.addAttribute("aidDto", new AidDto());
//        // Add additional attributes needed for the form (e.g., aid categories)
//        // Example: model.addAttribute("aidCategories", aidService.getAllAidCategories());
//        return "editAidForm"; // Assuming addAidForm.html is the Thymeleaf template
//    }
}
