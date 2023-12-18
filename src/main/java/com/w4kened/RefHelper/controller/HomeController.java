package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.models.AidCategoryEntity;
import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.models.UsersAidsEntity;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {

    public UserService userService;
    public AidService aidService;
    public UsersAidsService usersAidsService;

    private final AidCategoryRepository aidCategoryRepository;
    private final AidRepository aidRepository;
    private final UsersAidsRepository usersAidsRepository;

    @Autowired
    public HomeController(UserService userService, AidService aidService, UsersAidsService usersAidsService, AidCategoryRepository aidCategoryRepository, AidRepository aidRepository, UsersAidsRepository usersAidsRepository) {
        this.userService = userService;
        this.aidService = aidService;
        this.usersAidsService = usersAidsService;
        this.aidCategoryRepository = aidCategoryRepository;
        this.aidRepository = aidRepository;
        this.usersAidsRepository = usersAidsRepository;
    }


    @GetMapping("/home")
    public String getHomePage(Model model) throws NotFoundException {
        UserEntity user = new UserEntity();

        String email = SecurityUtil.getSessionUser();

//        userService.findByEmail(email).getRoleEntity();
        model.addAttribute("data", email);

        UserEntity userEntity = userService.findByEmail(email);

        String roleName = userEntity.getRoleEntity().getName();
        model.addAttribute("role", roleName);

        String userFullName = userEntity.getName();
        model.addAttribute("userFullName", userFullName);

//        System.out.println("username = "+email);
//        System.out.println("role = "+userService.findByEmail(email).getId());

        if (Objects.equals(userEntity.getRoleEntity().getName(), "ROLE_VOLUNTEER")) {
            List<AidEntity> aids = new ArrayList<>();
            //
            aids = aidService.findByCreatorUserId(userEntity.getId());

            System.out.println("aids= "+aids);

            List<Long> aidIds = aids.stream()
                                            .map(AidEntity::getId)
                                            .toList();
//            requestedAids = aidService.findAll()
            //find requested by aid Id

            System.out.println("ids= "+aidIds);

            List<UsersAidsEntity> requests = new ArrayList<>();



            if (!aids.isEmpty()) {
                requests = aidService.findRequestedAidsByAidIds(aidIds);
                for (int i = 0; i < requests.size(); i++) {
                    System.out.println("requests " + requests.get(i).getAidEntity().getId());
                }
            }

//            System.out.println("requests "+requests.get(0).getId());
//            requestedAids.get(0).getUsersAidsEntities().get()

//            List<AidDto> aidDtos  = new ArrayList<>();

            // Convert list to array
//            String[] stringArray = requestedAids.toArray(new AidEntity[0]);

            // Using Arrays.toString() to print array elements
//            System.out.println("Array as string: " + Arrays.toString(stringArray));

//            System.out.print("requestedAids=[");
//                for (Integer i = 0; i < requestedAids.size(); i++) {
//                    System.out.println(requestedAids.get(i).getClass() );
//                }
//            System.out.print("]\n");



//            System.out.println("cout: "+aids.toArray().length);

            System.out.println("requests = "+ requests);
            model.addAttribute("aidsOfferedCount", aids.toArray().length);
            model.addAttribute("aidsList", aids);
            model.addAttribute("requestedAidsList", requests);
            model.addAttribute("layout", "layout");

        } else {
            List<AidEntity> aids = new ArrayList<>();
            aids = aidService.findAll();
            model.addAttribute("aidsList", aids);

            //need to find responses which related tu refugees

//            aids = aidService.findByCreatorUserId(userEntity.getId());

            System.out.println("aids= "+aids);
//
////            List<User>
//            AidEntity aid = new AidEntity();
//            aid.getLatitude();
//            UsersAidsEntity usersAidsEntity = new UsersAidsEntity();
//            usersAidsEntity.getAidEntity().getLatitude();
//            usersAidsEntity.getAidEntity().getCreatedDate()
//            List<AidEntity> aidEntity = new ArrayList<>();
//            aidEntity.get(1).getAidCategoryEntity()



            List<UsersAidsEntity> responses = new ArrayList<>();
            responses = usersAidsService.findResponsesByUserId(userEntity.getId());

//            if (!aids.isEmpty()) {
//                responses = aidService.findRequestedAidsByAidIds(aidIds);
//                for (int i = 0; i < responses.size(); i++) {
//                    System.out.println("requests " + responses.get(i).getAidEntity().getId());
//                }
//            }
            model.addAttribute("responsesAidsList", responses);

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
        System.out.println("post = "+ aidDto);

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

    @GetMapping("/requestAid/{id}")
    public String requestAid(@PathVariable("id") Long id) throws NotFoundException  {
        if (aidService.countRequestedAidByUser(id) < 1) {
            aidService.requestAid(id);
            return "redirect:/home?AidRequestedSuccessfull";
        }
            return "redirect:/home";
    }

    @GetMapping("/acceptAidRequest/{aidId}/{userId}")
    public String acceptAidRequest(@PathVariable("aidId") Long aidId, @PathVariable("userId") Long userId) throws NotFoundException {
        // Use the 'id' and 'otherId' values in your method logic
        try {
            aidService.acceptAidRequest(aidId, userId);
            return "redirect:/home?AidRequestedAccepted";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
    @GetMapping("/rejectAidRequest/{aidId}/{userId}")
    public String rejectAidRequest(@PathVariable("aidId") Long aidId, @PathVariable("userId") Long userId) throws NotFoundException {
        try {
            aidService.rejectAidRequest(aidId, userId);
            return "redirect:/home";
        } catch (Exception ex) {
            return ex.getMessage();
        }

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
