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

}
