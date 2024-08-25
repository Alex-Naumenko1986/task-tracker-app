package com.alex.task_manager.io.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию нового пользователя")
public class UserRequest {
    @NotBlank(message = "Email should not be null")
    @Email(message = "Please enter valid email")
    @Schema(description = "Email нового пользователя", example = "test@yandex.ru")
    private String email;
    @NotNull(message = "Password should not be null")
    @Size(min = 6, message = "Password should be at least 6 characters long")
    @Schema(description = "Пароль", example = "123456")
    private String password;
}
