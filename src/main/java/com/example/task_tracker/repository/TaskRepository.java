package com.example.task_tracker.repository;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByOwnerId(Long ownerId);

    Optional<Task> findByIdAndOwnerId(Long id, Long ownerId);

    List<Task> findAllByOwnerIdAndStatus(Long ownerId, TaskStatus status);

    List<Task> findAllByOwnerIdAndDueDateBefore(Long ownerId, LocalDate date);

    boolean existsByIdAndOwnerId(Long id, Long ownerId);

    long countByOwnerIdAndStatus(Long ownerId, TaskStatus status);
}