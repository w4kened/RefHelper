package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.RoleEntity;
import com.w4kened.RefHelper.models.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository  extends CrudRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}
