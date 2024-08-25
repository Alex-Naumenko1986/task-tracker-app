package com.alex.task_manager.io.comment;

import com.alex.task_manager.io.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ сервера, содержащий информацию о комментарии")
public class CommentResponse {
    @Schema(description = "Идентификатор комментария", example = "550e8400-e29b-41d4-a716-446655440000 ")
    private String commentId;
    @Schema(description = "Текст комментария", example = "Это была очень интересная задача")
    private String text;
    @Schema(description = "Информация об авторе комментария")
    private UserResponse author;
}
