package com.alex.task_manager.repository;

import com.alex.task_manager.entity.TaskEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, QuerydslPredicateExecutor<TaskEntity> {

    Optional<TaskEntity> findByTaskIdAndAuthor_Id(String taskId, Long id);

    Optional<TaskEntity> findByTaskId(String taskId);

    List<TaskEntity> findByAuthor_Id(Long id, Pageable pageable);

    List<TaskEntity> findByAuthor_IdNot(Long id, Pageable pageable);
}
