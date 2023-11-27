package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.AidInteraction;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import org.springframework.data.repository.CrudRepository;

public interface UsersAidsRepository extends CrudRepository <UsersAidsEntity, Long> {
    UsersAidsEntity findByAidInteraction(AidInteraction aidInteraction);
}
