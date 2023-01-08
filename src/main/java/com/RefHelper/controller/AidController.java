package com.RefHelper.controller;

import com.RefHelper.entity.Aid;
import com.RefHelper.service.AidService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aid")
public class AidController {


    @Autowired(required = true)
    private AidService aidService;


    //sprawdzic zawartość listy w inny sposob
    @GetMapping
    public List<Aid> findAll() {
        return aidService.findAll();
    }


}
