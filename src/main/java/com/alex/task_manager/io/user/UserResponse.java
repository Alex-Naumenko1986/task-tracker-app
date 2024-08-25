package com.alex.task_manager.io.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ при создании нового пользователя")
public class UserResponse {
    @Schema(description = "ID нового пользователя", example = "1")
    private Long id;
    @Schema(description = "Email нового пользователя", example = "test@yandex.ru")
    private String email;
}
