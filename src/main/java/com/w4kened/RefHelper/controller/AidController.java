package com.w4kened.RefHelper.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.w4kened.RefHelper.dto.AidDto;
import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import com.w4kened.RefHelper.service.AidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AidController {

    private final AidService aidService;

    @Autowired
    public AidController(AidService aidService) {
        this.aidService = aidService;
    }


    @GetMapping("/getAllAidMarkers")
    public List<AidEntity> getAllAidMarkers() {
        //TODO need to provide id of user/creator through the useraidstable
//        List<AidEntity> aidEntities = aidService.findAll();
//        List<AidDto> aidDTOs = new ArrayList<>();
//
//        for (AidEntity aidEntity : aidEntities) {
//            AidDto aidDTO = new AidDto();
//            aidDTO.setAidEntity(aidEntity);
//
//            List<Long> userIds = aidEntity
//                    .getUsersAidsEntities()
//                    .stream()
//                    .map(usersAidsEntity ->
//                            usersAidsEntity
//                                    .getUserEntity()
//                                    .getId()
//                    )
//                    .collect(Collectors.toList());
//
//            aidDTO.setUserIds(userIds);
//            aidDTOs.add(aidDTO);
//        }
//
//        return aidDTOs;
//
//
//                Map<Long, AidEntity> userIdsAndAids = new HashMap<>();
//        // Iterate through the AidEntities and fetch associated user IDs
//        for (AidEntity aidEntity : aidEntities) {
//            List<UsersAidsEntity> usersAidsEntities = aidEntity.getUsersAidsEntities();
//            System.out.println("aidEntity "+ aidEntity);
//            System.out.println("usersAidsEntities "+ usersAidsEntities);
////
////            List<Long> userIds = new ArrayList<>();
////            for (UsersAidsEntity usersAidsEntity : usersAidsEntities) {
////                if (usersAidsEntity.getAidEntity().getId() == aidEntity.getId()) {
////                    userIdsAndAids.put(usersAidsEntity.getU)
////                }
////                Long userId = usersAidsEntity.getUserEntity().getId();
////                userIds.add(userId);
////            }
//
//            // Set the list of user IDs associated with this AidEntity
////            aidEntity.setUserIds(userIds);
//        }

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
            return "Aid successful loaded and saved to database.";
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
//        }
//            return ResponseEntity.internalServerError().body("error with deleting");
    }
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
