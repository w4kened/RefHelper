package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import javassist.NotFoundException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface AidRepository extends CrudRepository<AidEntity, Long> {
    @Query(value = "select * from aid_table", nativeQuery = true)
    List<AidEntity> findAll();


    //@Query(value = "SELECT at FROM aid_table AS at " +
//        "INNER JOIN users_aids_table AS uat " +
//        "ON at.id = uat.aid_id " +
//        "WHERE aid_interaction = 'REQUESTING' " +
//        "AND at.id IN :aidIds")
//    @Query(value = "SELECT * FROM aid_table at " +
//            "INNER JOIN users_aids_table uat " +
//            "ON uat.aid_id = at.id " +
//            "INNER JOIN user_table ut " +
//            "ON ut.id = uat.user_id " +
//            "WHERE uat.aid_interaction = 'REQUESTING' " +
//            "AND at.id IN :aidIds", nativeQuery = true)
//    List<AidEntity> findRequestedAidsByAidIds(@Param("aidIds") List<Long> aidIds);
//    SELECT *
//    FROM aid_table at
//    INNER JOIN users_aids_table uat ON at.id = uat.aid_id
//    INNER JOIN user_table ut ON uat.user_id = ut.id
//    WHERE uat.aid_interaction = 'REQUESTING'
//    AND at.id IN (SELECT at2.id FROM aid_table at2 WHERE at2.id = 3);


//    AidEntity findByCategoryAidEntity(String name);
//                                                        <td><span th:text="${aid.aidCategoryEntity.getName()}">Aid Category</span></td>
//                                                        <td><span id="descId" th:text="${aid.description}">Aid Description</span></td>
//<!--                                                        <td><span id="addressId" th:text="${aid.address}">Aid Address</span></td>-->
//                                                        <td><span id="descId" th:text="${aid.description}">Refugee name</span></td>
//                                                        <td><span id="descId" th:text="${aid.description}">Refugee phone</span></td>
////    @Modifying
////    @Transactional
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

//    @Query(value = "SELECT * FROM aid_table where id = ?1", nativeQuery = true)
    @Query(value = "SELECT COUNT(*) FROM aid_table AS at " +
            "INNER JOIN users_aids_table AS uat " +
            "ON at.id = uat.aid_id " +
            "WHERE uat.aid_interaction = 'REQUESTING' " +
            "AND at.id = ?1 " + // Added space before AND
            "AND uat.user_id = ?2 ", nativeQuery = true)
    Long countRequestedAidByAidIdAndUserId(Long aidId, Long userId);


//    List<UsersAidsEntity> findsResponsedAidsByAidIds(Long userId) throws NotFoundException;
}
