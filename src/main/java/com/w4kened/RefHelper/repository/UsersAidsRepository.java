package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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
}
