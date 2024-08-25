package com.alex.task_manager.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ при возникновении ошибки")
public class ErrorResponse {
    @Schema(description = "Код ответа", example = "400")
    private Integer statusCode;
    @Schema(description = "Сообщение об ошибке", example = "Please enter valid email")
    private String message;
    @Schema(description = "Время ошибки", example = "2024-08-21T20:50:53.6881046")
    private LocalDateTime timestamp;
}
