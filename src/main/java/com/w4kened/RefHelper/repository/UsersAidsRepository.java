package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UsersAidsRepository extends CrudRepository <UsersAidsEntity, Long> {
    @Query(value = "select * from users_aids_table", nativeQuery = true)
    List<UsersAidsEntity> findAll();
    @Query(value = "select * from users_aids_table where aid_interaction = ?1", nativeQuery = true)
    List<UsersAidsEntity> findByAidInteraction(AidInteraction aidInteraction);

    @Query(value = "select * from users_aids_table where aid_id = ?1", nativeQuery = true)
    List<UsersAidsEntity> findByAidId(Long id);

    @Query(value = "select * from users_aids_table where user_id = ?1", nativeQuery = true)
    List<UsersAidsEntity> findByUserId(Long id);

    @Query(value = "select * from users_aids_table where user_id = ?1 and aid_id = ?2", nativeQuery = true)
    List<UsersAidsEntity> findByUserIdAndAidId(Long userId, Long aidId);

    @Modifying
    @Transactional
    @Query(value = "delete from users_aids_table where id = ?1")
    void deleteById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from users_aids_table where aid_id = ?1")
    void deleteByAidId(Long id);

    @Query(value = "SELECT * FROM users_aids_table uat " +
            "INNER JOIN aid_table at ON uat.aid_id = at.id " +
            "INNER JOIN user_table ut ON ut.id = uat.user_id " +
            "WHERE uat.aid_interaction = 'REQUESTING' " +
            "AND uat.aid_id  NOT IN (" +
            "   SELECT uat2.aid_id FROM users_aids_table uat2 " +
            "   WHERE uat2.aid_interaction = 'ACCEPTANCE' " +
            "   OR    uat2.aid_interaction = 'REJECTION'" +
            ") " +
            "AND uat.aid_id IN :aidIds", nativeQuery = true)
    List<UsersAidsEntity> findRequestedAidsByAidIds(@Param("aidIds") List<Long> aidIds);

    @Query(value = "SELECT COUNT(*) " +
            "FROM users_aids_table " +
            "WHERE aid_id = ?1 " +
            "AND user_id = ?2 " +
            "AND aid_interaction = 'CREATING'", nativeQuery = true)
    Integer isCreatorOfAid(Long aidId, Long userId);



    @Query(value = "SELECT * FROM users_aids_table uat " +
            "INNER JOIN aid_table at ON uat.aid_id = at.id " +
            "WHERE uat.user_id = ?1 " +
            "AND uat.aid_interaction = 'ACCEPTANCE' " +
            "OR  uat.aid_interaction = 'REJECTION' ", nativeQuery = true)
    List<UsersAidsEntity> findResponsesByUserId(Long userId);





    @Query(value = "SELECT DATE_FORMAT(created_date, '%m-%Y') AS date, COUNT(id) AS count " +
            "FROM users_aids_table " +
            "WHERE aid_interaction = 'REQUESTING' "+
            "GROUP BY MONTH(created_date), YEAR(created_date)", nativeQuery = true)
    List<Object[]> getCountOfAidRequestsByMonthAndYear();

    @Query(value = "SELECT act.name AS Category, count(uat.id) AS count " +
            "FROM users_aids_table uat " +
            "INNER JOIN aid_table at ON uat.aid_id  = at.id " +
            "INNER JOIN aid_category_table act ON at.category_id = act.id " +
            "WHERE uat.aid_interaction = 'REQUESTING' " +
            "GROUP BY act.name ",nativeQuery = true)
    List<Object[]> getCountOfAidRequestsByMostRequestedCategory();

}
