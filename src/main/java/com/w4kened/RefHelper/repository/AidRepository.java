package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.AidEntity;
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

}
