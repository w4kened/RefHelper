package com.RefHelper.repository;

import com.RefHelper.entity.Aid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AidRepository extends JpaRepository <Aid, Long> {
    @Query(value = "select * from aid", nativeQuery = true)
    List<Aid> findAll();


    //@Query(value = "select * from aid where category_id = 1", nativeQuery = true)
    //List<Aid> find
}
