package com.w4kened.RefHelper.service.impl;

import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import com.w4kened.RefHelper.repository.UsersAidsRepository;
import com.w4kened.RefHelper.service.UsersAidsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersAidsServiceImpl implements UsersAidsService {
    @Autowired
    private final UsersAidsRepository usersAidsRepository;

    @Autowired
    public UsersAidsServiceImpl(UsersAidsRepository usersAidsRepository) {
        this.usersAidsRepository = usersAidsRepository;
    }


    @Override
    public List<UsersAidsEntity> findByAidId(Long id) {
        return usersAidsRepository.findByAidId(id);
    }

    @Override
    public List<UsersAidsEntity> findByAid(Long aid) {
        return null;
    }

    @Override
    public List<UsersAidsEntity> findByAidInteraction(AidInteraction aidInteraction) {
        return usersAidsRepository.findByAidInteraction(aidInteraction);
    }


}
