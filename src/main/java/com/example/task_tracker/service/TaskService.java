package com.example.task_tracker.service;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.TaskDtos.CreateTaskRequest;
import com.example.task_tracker.dto.TaskDtos.TaskResponse;
import com.example.task_tracker.dto.TaskDtos.UpdateTaskRequest;
import com.example.task_tracker.exception.ResourceNotFoundException;
import com.example.task_tracker.mapper.TaskMapper;
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

    @Transactional
    public TaskResponse create(CreateTaskRequest req, User owner) {
        Task saved = taskRepository.save(TaskMapper.toEntity(req, owner));
        return TaskMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> listForUser(User owner) {
        return taskRepository.findAllByOwnerId(owner.getId())
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getById(Long id, User owner) {
        return TaskMapper.toResponse(loadOwnedTask(id, owner));
    }

    @Transactional
    public TaskResponse update(Long id, UpdateTaskRequest req, User owner) {
        Task task = loadOwnedTask(id, owner);
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setStatus(req.status());
        task.setDueDate(req.dueDate());
        return TaskMapper.toResponse(task);
    }

    @Transactional
    public void delete(Long id, User owner) {
        taskRepository.delete(loadOwnedTask(id, owner));
    }

    // The single place that enforces the ownership rule.
    private Task loadOwnedTask(Long id, User owner) {
        return taskRepository
                .findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }
}