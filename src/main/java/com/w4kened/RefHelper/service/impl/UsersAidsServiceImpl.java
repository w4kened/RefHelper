package com.w4kened.RefHelper.service.impl;

import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import com.w4kened.RefHelper.repository.UsersAidsRepository;
import com.w4kened.RefHelper.service.UsersAidsService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<UsersAidsEntity> findByUserId(Long id) {
        return usersAidsRepository.findByAidId(id);
    }

    @Override
    public List<UsersAidsEntity> findByUserIdAndAidId(Long userId, Long aidId) {
        return usersAidsRepository.findByUserIdAndAidId(userId, aidId);
    }

    @Override
    public List<UsersAidsEntity> findResponsesByUserId(Long userId) throws NotFoundException {
        return usersAidsRepository.findResponsesByUserId(userId);
    }

//    @Override
//    public List<Object[]> getCountOfAidRequestsByMonthAndYear() {
//        return usersAidsRepository.getCountOfAidRequestsByMonthAndYear();
//    }

    @Override
    public Map<String, Long> getOverallDataForChart() {
        List<Object[]> result = usersAidsRepository.getCountOfAidRequestsByMonthAndYear();
        return result
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],  // Assuming 'date' is in the first position of the row
                        row -> ((BigInteger) row[1]).longValue() // Cast BigInteger to long
                ));
    }

    @Override
    public Map<String, Long> getMostRequestedDataForChart() {
        List<Object[]> result = usersAidsRepository.getCountOfAidRequestsByMostRequestedCategory();
        return result
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((BigInteger) row[1]).longValue()
                ));
    }
//    getOverallDataForChart

//    private DataPoint mapToDataPoint(Object[] row) {
//        String date = (String) row[0];
//        BigInteger count = (BigInteger) row[1]; // Use BigInteger instead of Long
//        return new DataPoint(date, count.longValue()); // Convert BigInteger to long
//    }

}
