package com.alex.task_manager.io.task;

import com.alex.task_manager.constant.Priority;
import com.alex.task_manager.constant.Status;
import com.alex.task_manager.io.comment.CommentResponse;
import com.alex.task_manager.io.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Ответ при запросе информации о задаче")
public class TaskResponse {
    @Schema(description = "Идентификатор задачи", example = "550e8400-e29b-41d4-a716-446655440000")
    private String taskId;
    @Schema(description = "Заголовок задачи", example = "Купить продукты")
    private String title;
    @Schema(description = "Описание задачи", example = "Купить продукты в магазине Перекресток")
    private String description;
    @Schema(description = "Статус задачи", example = "WAITING")
    private Status status;
    @Schema(description = "Приоритет задачи", example = "MEDIUM")
    private Priority priority;
    @Schema(description = "Автор задачи")
    private UserResponse author;
    @Schema(description = "Исполнитель задачи")
    private UserResponse performer;
    @Schema(description = "Комментарии к задаче")
    private List<CommentResponse> comments;
}
