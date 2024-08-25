package com.alex.task_manager.entity;

import com.alex.task_manager.constant.Priority;
import com.alex.task_manager.constant.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private String taskId;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_priority")
    private Priority priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performer_id")
    private UserEntity performer;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;
}
