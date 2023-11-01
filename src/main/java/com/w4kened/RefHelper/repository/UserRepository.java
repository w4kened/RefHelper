package com.w4kened.RefHelper.repository;

import com.w4kened.RefHelper.models.UserEntity;
import org.springframework.data.repository.CrudRepository;
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
