package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.CityEntity;
import com.w4kened.RefHelper.models.RoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<CityEntity, Long>  {

    @Query(value = "select * from city_table where name = ?1", nativeQuery = true)
    CityEntity findByName(String name);
}
