package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.dto.AidDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public HomeController(UserService userService, AidService aidService,
                          UsersAidsService usersAidsService,
                          AidCategoryRepository aidCategoryRepository,
                          AidRepository aidRepository,
                          UsersAidsRepository usersAidsRepository) {
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
        model.addAttribute("data", email);
        UserEntity userEntity = userService.findByEmail(email);

        String roleName = userEntity.getRoleEntity().getName();
        model.addAttribute("role", roleName);
        String userFullName = userEntity.getName();
        model.addAttribute("userFullName", userFullName);

        List<AidEntity> aids;
        List<Long> aidIds;
        if (Objects.equals(roleName, "ROLE_VOLUNTEER")) {
            aids = aidService.findByCreatorUserId(userEntity.getId());
            aidIds = aids.stream()
                                            .map(AidEntity::getId)
                                            .toList();
            List<UsersAidsEntity> requests = new ArrayList<>();
            if (!aids.isEmpty()) {
                requests = aidService.findRequestedAidsByAidIds(aidIds);
            }
            model.addAttribute("aidsOfferedCount", aids.toArray().length);
            model.addAttribute("aidsRequestedCount", requests.toArray().length);
            model.addAttribute("aidsList", aids);
            model.addAttribute("requestedAidsList", requests);
            model.addAttribute("layout", "layout");
        } else {
            aids = aidService.findAll();
            model.addAttribute("aidsList", aids);
            List<UsersAidsEntity> responses = new ArrayList<>();
            List<AidEntity> requestedAids = aidService.findByRequesterUserId(userEntity.getId());

            responses = usersAidsService.findResponsesByUserId(userEntity.getId());
            model.addAttribute("responsesAidsList", responses);
            model.addAttribute("aidsUnansweredCount", requestedAids.toArray().length);
            model.addAttribute("aidsResponsedCount", responses.toArray().length);
            model.addAttribute("layout", "refLayout");
        }
        return "home";
    }


    @GetMapping("/getTotalServiceRequests")
    public ResponseEntity<Map<String, Long>> getTotalServiceRequests() {
        Map<String, Long> dataForChart = usersAidsService.getOverallDataForChart();
        return ResponseEntity.ok().body(dataForChart);
    }

    @GetMapping("/getMostRequestedServices")
    public ResponseEntity<Map<String, Long>> getMostRequestedServices() {
        Map<String, Long> dataForChart = usersAidsService.getMostRequestedDataForChart();
        return ResponseEntity.ok().body(dataForChart);
    }

    @GetMapping("/getRegionalDistributionOfRefugees")
    public ResponseEntity<Map<String, Long>> getRegionalDistributionOfRefugees() {
        Map<String, Long> dataForChart = userService.getRegionalDistributionOfRefugeesForChart();
        return ResponseEntity.ok().body(dataForChart);
    }

    @GetMapping("/getRegionalDistributionOfAids")
    public ResponseEntity<Map<String, Long>> getRegionalDistributionOfAids() {
        Map<String, Long> dataForChart = aidService.getRegionalDistributionOfAidsForChart();
        return ResponseEntity.ok().body(dataForChart);
    }

    @GetMapping("/addAid")
    public String showAddAidForm(Model model) {
        model.addAttribute("aidDto", new AidDto());
        return "addAidForm";
    }

    @PostMapping("/addAid")
    public String addAid(@ModelAttribute("aidDto") AidDto aidDto) {
//        System.out.println("Received AidDto: " + aidDto); // Log to check if data is received correctly
        aidService.saveAid(aidDto);
        return "redirect:/home";
    }

    @GetMapping("/editAid/{id}")
    public ModelAndView showEditAidForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        AidEntity aidEntity = aidService.findByAidId(id);
        if (aidEntity == null) {
            throw new NotFoundException("Not aid with   ID " + id);
        }
        ModelAndView editView = new ModelAndView();
        editView.setViewName("editAidForm");
        AidDto aidDto = new AidDto(aidEntity);
        editView.addObject(aidDto);

        return editView;
    }



    @PostMapping("/editAid/{id}")
    public ModelAndView editAid(@PathVariable("id") Long id, @ModelAttribute("aidDto") AidDto aidDto) throws NotFoundException {
        ModelAndView editView = new ModelAndView();
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
        try {
            aidService.acceptAidRequest(aidId, userId);
            return "redirect:/home?AidRequezstedAccepted";
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

}
