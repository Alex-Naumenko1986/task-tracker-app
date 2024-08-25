package com.alex.task_manager.io.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на добавление комментария к задаче")
public class CommentRequest {
    @NotBlank(message = "Comment text should not be blank")
    @Schema(description = "Текст комментария", example = "Это была очень интересная задача")
    private String text;
}
