package com.alex.task_manager.mapper.task;

import com.alex.task_manager.entity.TaskEntity;
import com.alex.task_manager.io.task.TaskRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskRequestMapper {
    TaskEntity mapToEntity(TaskRequest taskRequest);
}
