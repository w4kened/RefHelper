package com.w4kened.RefHelper.service.impl;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import com.w4kened.RefHelper.repository.UsersAidsRepository;
import com.w4kened.RefHelper.service.UsersAidsService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersAidsServiceImpl implements UsersAidsService {
    @Autowired
    private final UsersAidsRepository usersAidsRepository;

    @Autowired
    public UsersAidsServiceImpl(UsersAidsRepository usersAidsRepository) {
        this.usersAidsRepository = usersAidsRepository;
    }


    @Override
    public void deleteUsersAidsById(Long id) throws NotFoundException {
        Optional<UsersAidsEntity> optionalAidEntity = usersAidsRepository.findById(id);
        if (optionalAidEntity.isPresent()) {
            usersAidsRepository.deleteById(id); // Delete the aid by its ID
        } else {
            throw new NotFoundException("UsersAidsEntity with ID " + id + " not found");
        }
    }

    @Override
    public void deleteUsersAidsByAidId(Long id) throws NotFoundException {
        Optional<UsersAidsEntity> optionalAidEntity = usersAidsRepository.findById(id);
        if (optionalAidEntity.isPresent()) {
            usersAidsRepository.deleteByAidId(id); // Delete the aid by its ID
        } else {
            throw new NotFoundException("UsersAidsEntity with ID " + id + " not found");
        }
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
