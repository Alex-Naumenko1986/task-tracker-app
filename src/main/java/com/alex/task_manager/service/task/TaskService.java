package com.alex.task_manager.service.task;

import com.alex.task_manager.constant.Status;
import com.alex.task_manager.entity.TaskEntity;

import java.util.List;

public interface TaskService {
    TaskEntity createTask(TaskEntity task, String performerEmail);

    TaskEntity updateTask(String taskId, TaskEntity task, String performerEmail);

    TaskEntity getTaskById(String taskId);

    List<TaskEntity> getAllUserTasks(int page, int size);

    List<TaskEntity> getOtherUsersTasks(int page, int size);

    List<TaskEntity> filterByAuthorAndPerformer(String author, String performer, int page, int size);

    void removeTask(String taskId);

    TaskEntity updateTaskStatus(String taskId, Status status);

    TaskEntity addComment(String taskId, String text);
}
