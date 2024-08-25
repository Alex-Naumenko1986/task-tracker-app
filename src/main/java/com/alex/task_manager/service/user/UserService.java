package com.alex.task_manager.service.user;

import com.alex.task_manager.entity.UserEntity;

public interface UserService {
    UserEntity createUser(UserEntity user);

    UserEntity getLoggedInUser();

    UserEntity getUserByEmail(String email);
}
