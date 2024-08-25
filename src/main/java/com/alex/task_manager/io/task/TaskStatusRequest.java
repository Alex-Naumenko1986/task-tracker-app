package com.alex.task_manager.io.task;

import com.alex.task_manager.constant.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление статуса задачи")
public class TaskStatusRequest {
    @NotNull(message = "Field status should not be null")
    @Schema(description = "Новый статус задачи", example = "WAITING")
    private Status status;
}
