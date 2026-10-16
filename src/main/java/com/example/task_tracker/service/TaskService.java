package com.example.task_tracker.service;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.User;
import com.example.task_tracker.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> listForUser(User owner) {
        return taskRepository.findAllByOwnerId(owner.getId());
    }

    @Transactional(readOnly = true)
    public Task getById(Long id, User owner) {
        return loadOwnedTask(id, owner);
    }

    @Transactional
    public Task create(Task task, User owner) {
        task.setOwner(owner);
        return taskRepository.save(task);
    }

    @Transactional
    public void delete(Long id, User owner) {
        taskRepository.delete(loadOwnedTask(id, owner));
    }

    // The single place that enforces the ownership rule.
    private Task loadOwnedTask(Long id, User owner) {
        return taskRepository
                .findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
}