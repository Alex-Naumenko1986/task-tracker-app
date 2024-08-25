package com.alex.task_manager.mapper.task;

import com.alex.task_manager.entity.TaskEntity;
import com.alex.task_manager.io.task.TaskResponse;
import com.alex.task_manager.mapper.comment.CommentResponseMapper;
import com.alex.task_manager.mapper.user.UserResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentResponseMapper.class, UserResponseMapper.class},
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TaskResponseMapper {
    TaskResponse mapToResponse(TaskEntity taskEntity);

    List<TaskResponse> mapToResponseList(List<TaskEntity> taskEntities);
}
