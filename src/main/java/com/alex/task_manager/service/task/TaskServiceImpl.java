package com.alex.task_manager.service.task;

import com.alex.task_manager.constant.Status;
import com.alex.task_manager.entity.CommentEntity;
import com.alex.task_manager.entity.QTaskEntity;
import com.alex.task_manager.entity.TaskEntity;
import com.alex.task_manager.entity.UserEntity;
import com.alex.task_manager.exception.TaskNotFoundException;
import com.alex.task_manager.repository.TaskRepository;
import com.alex.task_manager.service.user.UserService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public TaskEntity createTask(TaskEntity task, String performerEmail) {
        task.setTaskId(UUID.randomUUID().toString());
        UserEntity author = userService.getLoggedInUser();
        task.setAuthor(author);
        if (performerEmail != null) {
            UserEntity performer = userService.getUserByEmail(performerEmail);
            task.setPerformer(performer);
        }
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public TaskEntity updateTask(String taskId, TaskEntity task, String performerEmail) {
        UserEntity author = userService.getLoggedInUser();
        TaskEntity oldTask = taskRepository.findByTaskIdAndAuthor_Id(taskId, author.getId())
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %s was not found " +
                        "for author with id: %d. May be this user is not the author of this task and can't " +
                        "change it", taskId, author.getId())));
        oldTask.setTitle(Objects.requireNonNullElse(task.getTitle(), oldTask.getTitle()));
        oldTask.setDescription(Objects.requireNonNullElse(task.getDescription(), oldTask.getDescription()));
        oldTask.setPriority(Objects.requireNonNullElse(task.getPriority(), oldTask.getPriority()));
        oldTask.setStatus(Objects.requireNonNullElse(task.getStatus(), oldTask.getStatus()));
        if (performerEmail != null) {
            UserEntity performer = userService.getUserByEmail(performerEmail);
            oldTask.setPerformer(performer);
        }
        return taskRepository.save(oldTask);
    }

    @Override
    @Transactional
    public TaskEntity getTaskById(String taskId) {
        return taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %s was not found", taskId)));
    }

    @Override
    @Transactional
    public List<TaskEntity> getAllUserTasks(int page, int size) {
        UserEntity user = userService.getLoggedInUser();
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByAuthor_Id(user.getId(), pageable);
    }

    @Override
    @Transactional
    public List<TaskEntity> getOtherUsersTasks(int page, int size) {
        UserEntity user = userService.getLoggedInUser();
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByAuthor_IdNot(user.getId(), pageable);
    }

    @Override
    @Transactional
    public List<TaskEntity> filterByAuthorAndPerformer(String authorEmail, String performerEmail, int page, int size) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(Expressions.asBoolean(true).isTrue());

        if (authorEmail != null) {
            UserEntity author = userService.getUserByEmail(authorEmail);
            predicates.add(QTaskEntity.taskEntity.author.id.eq(author.getId()));
        }

        if (performerEmail != null) {
            UserEntity performer = userService.getUserByEmail(performerEmail);
            predicates.add(QTaskEntity.taskEntity.performer.id.eq(performer.getId()));
        }

        Pageable pageable = PageRequest.of(page, size);

        Predicate predicate = ExpressionUtils.allOf(predicates);

        return taskRepository.findAll(predicate, pageable).toList();
    }

    @Override
    @Transactional
    public void removeTask(String taskId) {
        UserEntity user = userService.getLoggedInUser();
        TaskEntity task = taskRepository.findByTaskIdAndAuthor_Id(taskId, user.getId())
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %s was not found " +
                        "for author with id: %d. May be this user is not the author of this task and can't " +
                        "delete it", taskId, user.getId())));
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public TaskEntity updateTaskStatus(String taskId, Status status) {
        TaskEntity task = taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %s was not found", taskId)));
        UserEntity loggedUser = userService.getLoggedInUser();

        if (!Objects.equals(task.getAuthor().getId(), loggedUser.getId()) && ((task.getPerformer() == null) ||
                !Objects.equals(task.getPerformer().getId(), loggedUser.getId()))) {
            throw new UnsupportedOperationException(String.format("User with id: %d is not the author or performer of task with id: %s " +
                    "and can't change its status", loggedUser.getId(), taskId));
        }

        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public TaskEntity addComment(String taskId, String text) {
        TaskEntity task = taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id: %s was not found", taskId)));
        UserEntity author = userService.getLoggedInUser();
        CommentEntity comment = CommentEntity.builder().commentId(UUID.randomUUID().toString()).author(author)
                .text(text).build();
        comment.setTask(task);
        task.getComments().add(comment);
        return taskRepository.save(task);
    }
}
