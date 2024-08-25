package com.alex.task_manager.mapper.user;

import com.alex.task_manager.entity.UserEntity;
import com.alex.task_manager.io.user.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper {
    UserEntity mapToEntity(UserRequest userRequest);
}
