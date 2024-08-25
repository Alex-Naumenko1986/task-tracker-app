package com.alex.task_manager.io.task;

import com.alex.task_manager.constant.Priority;
import com.alex.task_manager.constant.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание или обновление задачи")
public class TaskRequest {

    @NotBlank(message = "Field title should not be blank")
    @Schema(description = "Заголовок задачи", example = "Купить продукты")
    private String title;

    @NotBlank(message = "Field description should not be blank")
    @Schema(description = "Описание задачи", example = "Купить продукты в магазине Перекресток")
    private String description;

    @NotNull(message = "Field status should not be null")
    @Schema(description = "Статус задачи", example = "WAITING")
    private Status status;

    @NotNull(message = "Field status should not be null")
    @Schema(description = "Приоритет задачи", example = "MEDIUM")
    private Priority priority;

    @Schema(description = "Email исполнителя задачи. Необязательное поле", example = "performer@yandex.ru",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String performerEmail;
}
