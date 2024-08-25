package com.alex.task_manager.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию")
public class AuthRequest {
    @NotBlank(message = "Field email should not be blank")
    @Email(message = "Invalid email")
    @Schema(description = "Email", example = "test@yandex.ru")
    private String email;
    @NotBlank(message = "Field password should not be blank")
    @Schema(description = "Пароль", example = "123456")
    private String password;
}
