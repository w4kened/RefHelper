package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.AidCategoryEntity;
import com.w4kened.RefHelper.models.AidEntity;
import org.springframework.data.repository.CrudRepository;

public interface AidCategoryRepository extends CrudRepository<AidCategoryEntity, Long> {
    AidCategoryEntity findByName(String name);
}
