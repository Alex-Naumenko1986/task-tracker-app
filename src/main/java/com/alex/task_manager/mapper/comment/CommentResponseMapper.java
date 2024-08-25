package com.alex.task_manager.mapper.comment;

import com.alex.task_manager.entity.CommentEntity;
import com.alex.task_manager.io.comment.CommentResponse;
import com.alex.task_manager.mapper.user.UserResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,
        uses = {UserResponseMapper.class})
public interface CommentResponseMapper {
    CommentResponse mapToResponse(CommentEntity commentEntity);

    List<CommentResponse> mapToResponseList(List<CommentEntity> commentEntities);
}
