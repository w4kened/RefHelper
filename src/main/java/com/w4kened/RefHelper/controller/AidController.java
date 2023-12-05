package com.w4kened.RefHelper.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import com.w4kened.RefHelper.repository.AidRepository;
import com.w4kened.RefHelper.service.AidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AidController {

    private final AidService aidService;
    private final AidRepository aidRepository;

    @Autowired
    public AidController(AidService aidService, AidRepository aidRepository) {
        this.aidService = aidService;
        this.aidRepository = aidRepository;
    }


    @GetMapping("/getAllAidMarkers")
    public List<AidEntity> getAllAidMarkers() {
        return aidService.findAll();
    }


    @PostMapping("/uploadAidData")
    public String uploadAidData(@RequestBody String jsonData) {
        // Опрацьовуємо JSON-дані та зберігаємо їх у базі даних
        // Ви можете використовувати бібліотеку, таку як Jackson, для розбору JSON в об'єкти
        // Потім викликаєте метод сервісу для збереження даних

        // Приклад: розбір JSON-даних та збереження маркера допомоги
        System.out.println("invoking insertAid");
        System.out.println(jsonData);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AidDto aidDto = objectMapper.readValue(jsonData, AidDto.class);
            aidService.saveAid(aidDto);
            return "redirect:/";
        } catch (JsonProcessingException e) {
            return "Error at aid processing from JSON JSON-даних: " + e.getMessage();
        }
    }
//

//
//    @PostMapping("/deleteAidDataByDescriptionAndLocation/{description}/{latitude}/{longitude}")
//    public ResponseEntity<?> deleteAid(@PathVariable String description, @PathVariable Double latitude, @PathVariable Double longitude) {
//        System.out.println("deleting with ["+description+"]");
//        aidService.removeAid(description, latitude, longitude);
//////        if (deletedRecords > 0) {
//            return ResponseEntity.ok().build();
////        }
////            return ResponseEntity.internalServerError().body("error with deleting");
//    }

    @PostMapping("/deleteAidById/{usersAidsId}/{aidId}")
    public ResponseEntity<?> deleteAid(@PathVariable Long usersAidsId, @PathVariable Long aidId ) {
        System.out.println("deleting with ["+usersAidsId+"]");
        System.out.println("deleting with ["+aidId+"]");
        aidService.removeAid(usersAidsId, aidId);
////        if (deletedRecords > 0) {
        return ResponseEntity.ok().build();
    }
//
//    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
//    public String getAid(Model model,@PathVariable("id") Long id){
//        AidEntity aidEntity = aidService.findByAidId(id);
//        AidDto aidDto = new AidDto(aidEntity);
//
//        model.addAttribute("aidDto",aidDto);
//        return"home";
//    }

//    @GetMapping("/create")
//    public String showCreateForm(Model model) {
////        AidEntity aidEntity = new AidEntity();
////        AidDto aidDto = new AidDto();
//        model.addAttribute("form", )
//    }

//    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
//    public RedirectView updateAid(RedirectAttributes redirectAttributes, @PathVariable("id") Integer id, @ModelAttribute UserInfo userInfo){
////        aidService.updateAid(id,userInfo);
////        String message=(userInfo.isActive()?"Updated ":"Deleted ")+" user <b>"+userInfo.getFirstName()+" "+userInfo.getLastName()+"</b> ✨.";
////        RedirectView redirectView=new RedirectView("/",true);
////        redirectAttributes.addFlashAttribute("userMessage",message);
//        return redirectView;
//    }
//
//    @GetMapping("/edit/{id}")
//    public String showUpdateForm(@PathVariable("id") long id, Model model) {
//        AidEntity aid = aidRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid aid Id:" + id));
//
//        model.addAttribute("aidNew", aid);
//        return "update-aid";
//    }
//
////    @PostMapping("/update/{id}")
//    public String updateAid(@PathVariable("id") long id, @Valid Aid aid,
//                            BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            aid.setId(id);
//            return "update-aid";
//        }
//
//        aidRepository.save(aid);
//        return "redirect:/index";
//    }

    @GetMapping("/delete/{id}")
    public String deleteAid(@PathVariable("id") long id, Model model) {
        AidEntity aid = aidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid aid Id:" + id));
        aidRepository.delete(aid);
        return "redirect:/index";
    }

//    @PostMapping("/updateAidById/{aidId}")
//    public ResponseEntity<?>  updateAid(@PathVariable Long aidId) {
//        System.out.println("find by id "+aidRepository.findById(aidId));
//
//        return ResponseEntity.ok().build();
//    }

//    @RequestMapping(value = "/editAid", method = RequestMethod.GET)
//    public String showEditAidPage(Model model, @RequestParam("id") Long id) {
//        AidEntity aid = aidService.findByAidId(id);
//        model.addAttribute("aid", aid);
//
//        return "editAid";
//    }



//    @RequestMapping(value = "/editAid", method = RequestMethod.POST)
//    public String saveAid(@ModelAttribute("aid") AidDto aid) {
//        aidService.saveAid(aid);
//        return "redirect:/aids";
//    }

//     Handler method to display the form for adding a new aid



//    // Handler method to process the submitted new aid form


    // Handler method to process the submitted new aid form


//    @GetMapping("/delete/{id}")
//    public String deleteAid(@PathVariable("id") long id, Model model) {
//        Aid aid = aidRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid aid Id:" + id));
//        aidRepository.delete(aid);
//        model.addAttribute("aidsList", aidRepository.findAll());
//        return "index";
//    }

//    @PostMapping("/deleteAidData")
//    public String deleteAid(@RequestBody String jsonData)  throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
//        System.out.println("invoking removeAid");
//        System.out.println(jsonData);
////        System.out.println(objectMapper.readValue(jsonData, AidDto.class));
////
////        try {
//            System.out.println("before aidDto from json ");
////
//            AidDto aidDto = objectMapper.readValue(jsonData, AidDto.class);
//////            System.out.println(objectMapper.readValue(jsonData, AidDto.class));
//            System.out.println("aidDto from json "+ aidDto);
////
//////            System.out.println("invoking removeAid");
////            aidService.removeAid();
//            return "Aid successful deleted from database.";
////        } catch (JsonProcessingException e) {
////            return "Error at aid processing from JSON JSON-data: " + e.getMessage();
////        }
//    }
}
