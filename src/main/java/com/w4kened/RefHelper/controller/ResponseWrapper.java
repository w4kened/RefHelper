package com.w4kened.RefHelper.controller;

import com.w4kened.RefHelper.models.AidEntity;
import com.w4kened.RefHelper.models.UserEntity;
import com.w4kened.RefHelper.models.UsersAidsEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ResponseWrapper {
    private List<AidEntity> aidEntities;
    private List<UsersAidsEntity> usersAidsEntities;

//    public ResponseWrapper(UserEntity user, AidEntity)
}
