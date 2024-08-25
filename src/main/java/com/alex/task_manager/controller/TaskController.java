package com.alex.task_manager.controller;

import com.alex.task_manager.entity.TaskEntity;
import com.alex.task_manager.exception.ErrorResponse;
import com.alex.task_manager.io.comment.CommentRequest;
import com.alex.task_manager.io.task.TaskRequest;
import com.alex.task_manager.io.task.TaskResponse;
import com.alex.task_manager.io.task.TaskStatusRequest;
import com.alex.task_manager.mapper.task.TaskRequestMapper;
import com.alex.task_manager.mapper.task.TaskResponseMapper;
import com.alex.task_manager.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Контроллер задач", description = "Контроллер для управления задачами")
public class TaskController {

    private final TaskService taskService;
    private final TaskRequestMapper taskRequestMapper;
    private final TaskResponseMapper taskResponseMapper;


    @PostMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Создание задачи", description = "Позволяет пользователю создать новую задачу")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Задача успешно создана",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Введены невалидные данные при создании задачи",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))}),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Пользователь, указанный как исполнитель, не найден",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))})})

    public TaskResponse createTask(@RequestBody @Valid @Parameter(description = "Запрос на создание задачи") TaskRequest taskRequest) {
        TaskEntity taskEntity = taskRequestMapper.mapToEntity(taskRequest);
        taskEntity = taskService.createTask(taskEntity, taskRequest.getPerformerEmail());
        return taskResponseMapper.mapToResponse(taskEntity);
    }

    @PutMapping(value = "/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновление задачи", description = "Позволяет пользователю обновить созданную им задачу," +
            "в том числе поменять статус и назначить исполнителя задачи")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Задача успешно обновлена",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Задача с таким идентификатором не найдена или " +
                            "пользователь не является автором задачи",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))})})

    public TaskResponse updateTask(@PathVariable @NotNull @Parameter(description = "Идентификатор задачи") String taskId,
                                   @RequestBody @Parameter(description = "Запрос на обновление задачи") TaskRequest taskRequest) {
        TaskEntity taskEntity = taskRequestMapper.mapToEntity(taskRequest);
        taskEntity = taskService.updateTask(taskId, taskEntity, taskRequest.getPerformerEmail());
        return taskResponseMapper.mapToResponse(taskEntity);
    }

    @GetMapping(value = "/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение задачи по ID", description = "Позволяет получить задачу по идентификатору")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Задача найдена",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Задача с таким идентификатором не найдена",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))})})

    public TaskResponse getTaskById(@PathVariable @NotNull @Parameter(description = "Идентификатор задачи") String taskId) {
        TaskEntity taskEntity = taskService.getTaskById(taskId);
        return taskResponseMapper.mapToResponse(taskEntity);
    }

    @GetMapping(value = "/tasks/my", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение всех задач пользователя", description = "Позволяет получить все задачи пользователя." +
            "Поддерживает пагинацию")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Задачи найдены",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = @Content)})

    public List<TaskResponse> getAllUserTasks(@RequestParam(required = false, defaultValue = "0")
                                              @Parameter(description = "номер страницы") Integer page,
                                              @RequestParam(required = false, defaultValue = "20")
                                              @Parameter(description = "размер страницы") Integer size) {
        List<TaskEntity> tasks = taskService.getAllUserTasks(page, size);
        return taskResponseMapper.mapToResponseList(tasks);
    }

    @GetMapping(value = "/tasks/others", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение всех задач других пользователей", description = "Позволяет получить все задачи " +
            " других пользователей. Поддерживает пагинацию")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Задачи найдены",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = @Content)})

    public List<TaskResponse> getOtherUsersTasks(@RequestParam(required = false, defaultValue = "0")
                                                 @Parameter(description = "номер страницы") Integer page,
                                                 @RequestParam(required = false, defaultValue = "20")
                                                 @Parameter(description = "размер страницы") Integer size) {
        List<TaskEntity> tasks = taskService.getOtherUsersTasks(page, size);
        return taskResponseMapper.mapToResponseList(tasks);
    }

    @DeleteMapping(value = "/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удаление задачи по ID", description = "Позволяет удалить задачу по идентификатору")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Задача удалена",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Задача с таким идентификатором не найдена или " +
                            "пользователь не является автором задачи",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))})})

    public void deleteTaskById(@PathVariable @NotNull @Parameter(description = "Идентификатор задачи") String taskId) {
        taskService.removeTask(taskId);
    }

    @PatchMapping(value = "/tasks/{taskId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Изменение статуса задачи", description = "Позволяет автору или исполнителю изменить статус" +
            "задачи")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Статус успешно обновлен",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Пользователь не является автором или исполнителем " +
                            "задачи и не может изменить ее статус",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))}),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Задача с таким идентификатором не найдена",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))})})

    public TaskResponse updateTaskStatus(@RequestBody @Valid @Parameter(description = "Запрос на обновление статуса задачи")
                                         TaskStatusRequest taskStatusRequest,
                                         @PathVariable @NotNull @Parameter(description = "Идентификатор задачи") String taskId) {
        TaskEntity taskEntity = taskService.updateTaskStatus(taskId, taskStatusRequest.getStatus());
        return taskResponseMapper.mapToResponse(taskEntity);
    }

    @PostMapping(value = "/tasks/{taskId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Добавление комментария к задаче", description = "Позволяет любому пользователю добавить " +
            "комментарий к задаче")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Задача с таким идентификатором не найдена",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))})})

    public TaskResponse addComment(@RequestBody @Valid @Parameter(description = "Запрос на добавление комментария")
                                   CommentRequest comment,
                                   @PathVariable @NotNull @Parameter(description = "Идентификатор задачи") String taskId) {
        TaskEntity taskEntity = taskService.addComment(taskId, comment.getText());
        return taskResponseMapper.mapToResponse(taskEntity);
    }

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получение всех задач конкретного автора или исполнителя", description = "Позволяет получить все " +
            "задачи определенного автора и/или исполнителя. Поддерживает пагинацию. Задачи выводятся со всеми комментариями")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Задачи найдены",
                    useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "404", description = "Автор или исполнитель с таким email не найден",
                            content = {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))}),
                    @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован", content = @Content)})

    public List<TaskResponse> getTasks(@RequestParam(required = false, defaultValue = "0")
                                       @Parameter(description = "номер страницы") Integer page,
                                       @RequestParam(required = false, defaultValue = "20")
                                       @Parameter(description = "размер страницы") Integer size,
                                       @RequestParam(required = false)
                                       @Parameter(description = "email автора задач, по которому производится фильтрация")
                                       String author,
                                       @RequestParam(required = false)
                                       @Parameter(description = "email исполнителя задач," +
                                               " по которому производится фильтрация")
                                       String performer) {
        List<TaskEntity> tasks = taskService.filterByAuthorAndPerformer(author, performer, page, size);
        return taskResponseMapper.mapToResponseList(tasks);
    }
}

