package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AidRepository extends CrudRepository<AidEntity, Long> {
    @Query(value = "select * from aid_table", nativeQuery = true)
    List<AidEntity> findAll();
//    AidEntity findByCategoryAidEntity(String name);

//    @Modifying
//    @Transactional
//    @Query(value = "delete from aid_table where description = ?1 and latitude = ?2 and longitude = ?3")
//    void deleteByDescriptionAndLatitudeAndLongitude(String description, Double latitude, Double longitude);
//

//    @Modifying
//    @Transactional
//    @Query(value = "delete from users_aids_table where id = ?1")
//    void deleteUsersAidsById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from aid_table where id = ?1")
    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query(value = "update aid_table set address = ?1, description = ?2, latitude = ?3, longitude = ?4, category_id = ?5 where id = ?6")
    void updateById(String address, String description, Double latitude, Double longitude, Long category_id, Long id);


    //    findByCreator
    @Query(value = "SELECT * from aid_table where id in (SELECT aid_id from users_aids_table WHERE aid_interaction = 'CREATING' AND user_id = ?1)", nativeQuery = true)
    List<AidEntity> findByCreatorUserId(Long userId);

    @Query(value = "SELECT * FROM aid_table where id = ?1", nativeQuery = true)
    AidEntity findByAidId(Long id);
}
