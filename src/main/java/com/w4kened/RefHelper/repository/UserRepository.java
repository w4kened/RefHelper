package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);


    @Query(value = "SELECT rt2.name AS Region, COUNT(ut.id) AS Count " +
            "FROM user_table ut " +
            "INNER JOIN role_table rt ON ut.role_id = rt.id " +
            "INNER JOIN city_table ct ON ut.city_id = ct.id " +
            "INNER JOIN region_table rt2  ON ct.region_id = rt2.id " +
            "WHERE rt.name = 'ROLE_REFUGEE' " +
            "GROUP BY rt2.name "
            , nativeQuery = true)
    List<Object[]> getRegionalDistributionOfRefugees();
}
