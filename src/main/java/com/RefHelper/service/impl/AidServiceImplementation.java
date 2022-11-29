package com.RefHelper.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.RefHelper.entity.Aid;
import com.RefHelper.repo.AidRepo;
import com.RefHelper.service.AidService;

@Service
public class AidServiceImplementation implements AidService {
    @Autowired
    private AidRepo aidRepo;

    public List<Aid> findAids() {
        return aidRepo.findAids();
    }
    

}
