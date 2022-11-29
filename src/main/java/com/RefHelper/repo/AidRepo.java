package com.RefHelper.repo;

import com.RefHelper.entity.Aid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AidRepo extends JpaRepository <Aid, Integer> {
    @Query(value = "select * from aid_table", nativeQuery = true)
    List<Aid> findAids();

}
