package com.alex.task_manager.mapper.user;

import com.alex.task_manager.entity.UserEntity;
import com.alex.task_manager.io.user.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponse mapToResponse(UserEntity userEntity);
}
