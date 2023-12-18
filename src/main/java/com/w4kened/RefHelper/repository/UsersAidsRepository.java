package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
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
//    @Query(value = "select * from users_aids_table where aid_id = ?1", nativeQuery = true)
//    List<UsersAidsEntity> findByAidId(Long id);
    @Query(value = "select * from users_aids_table where aid_id = ?1", nativeQuery = true)
    List<UsersAidsEntity> findByAidId(Long id);

    @Query(value = "select * from users_aids_table where user_id = ?1", nativeQuery = true)
    List<UsersAidsEntity> findByUserId(Long id);

    @Query(value = "select * from users_aids_table where user_id = ?1 and aid_id = ?2", nativeQuery = true)
    List<UsersAidsEntity> findByUserIdAndAidId(Long userId, Long aidId);


//
//    @Modifying
//    @Transactional
//    @Query(value = "delete from aid_table where id = ?1")
//    void findById(Long id);
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
            "AND ut.id NOT IN (" +
            "   SELECT ut2.id FROM user_table ut2 " +
            "   INNER JOIN users_aids_table uat2 ON uat2.user_id = ut2.id " +
            "   WHERE uat2.aid_interaction = 'ACCEPTED'" +
            " " +
            ")" +
            "AND uat.aid_id IN :aidIds", nativeQuery = true)
    List<UsersAidsEntity> findRequestedAidsByAidIds(@Param("aidIds") List<Long> aidIds);

}
