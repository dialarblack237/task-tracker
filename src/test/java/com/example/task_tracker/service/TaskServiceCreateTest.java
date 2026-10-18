package com.example.task_tracker.service;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.TaskStatus;
import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.TaskDtos.CreateTaskRequest;
import com.example.task_tracker.dto.TaskDtos.TaskResponse;
import com.example.task_tracker.dto.TaskDtos.UpdateTaskRequest;
import com.example.task_tracker.exception.ResourceNotFoundException;
import com.example.task_tracker.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.task_tracker.support.Users.withId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceCreateTest {

    @Mock TaskRepository taskRepository;
    @InjectMocks TaskService taskService;

    @Test
    void create_setsOwnerAndDefaultsStatusTodo() {
        // GIVEN
        User owner = withId(42L);
        CreateTaskRequest req = new CreateTaskRequest("Write README", "docs", null);

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        TaskResponse res = taskService.create(req, owner);

        // THEN
        assertThat(res).isNotNull();
        assertThat(res.title()).isEqualTo("Write README");
        assertThat(res.status()).isEqualTo(TaskStatus.TODO);

        // Persisted-state assertion via ArgumentCaptor
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task persisted = captor.getValue();
        assertThat(persisted.getOwner()).isEqualTo(owner);
        assertThat(persisted.getStatus()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void getById_throws_whenTaskNotFound() {
        // GIVEN
        User owner = withId(42L);
        when(taskRepository.findByIdAndOwnerId(99L, 42L))
                .thenReturn(Optional.empty());

        // WHEN / THEN
        assertThatThrownBy(() -> taskService.getById(99L, owner))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");   // capital T — matches your TaskService
    }

    @Test
    void listForUser_chainedAssertions() {
        // GIVEN
        User owner = withId(42L);
        when(taskRepository.findAllByOwnerId(42L)).thenReturn(java.util.List.of());

        // WHEN
        var result = taskService.listForUser(owner);

        // THEN — chained on the same subject
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void delete_removesTask_whenOwned() {
        // GIVEN
        User owner = withId(42L);
        Task task = new Task();
        task.setTitle("Bye");
        task.setOwner(owner);

        when(taskRepository.findByIdAndOwnerId(1L, 42L)).thenReturn(Optional.of(task));

        // WHEN
        taskService.delete(1L, owner);

        // THEN
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).delete(captor.capture());
        assertThat(captor.getValue()).isSameAs(task);
    }

    @Test
    void delete_throws_whenTaskNotFound() {
        // GIVEN
        User owner = withId(42L);
        when(taskRepository.findByIdAndOwnerId(1L, 42L)).thenReturn(Optional.empty());

        // WHEN/THEN
        assertThatThrownBy(() -> taskService.delete(1L, owner))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");
    }

    @Test
    void update_appliesChangesAndReturnsResponse() {
        // GIVEN
        User owner = withId(42L);
        Task existing = new Task();
        existing.setTitle("Old");
        existing.setOwner(owner);

        when(taskRepository.findByIdAndOwnerId(1L, 42L)).thenReturn(Optional.of(existing));

        UpdateTaskRequest req = new UpdateTaskRequest("New", "desc", TaskStatus.DONE, null);

        // WHEN
        TaskResponse res = taskService.update(1L, req, owner);

        // THEN
        assertThat(res).isNotNull();
        assertThat(existing.getTitle()).isEqualTo("New");
        assertThat(existing.getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void update_throws_whenTaskNotFound() {
        // GIVEN
        User owner = withId(42L);
        when(taskRepository.findByIdAndOwnerId(1L, 42L)).thenReturn(Optional.empty());

        // WHEN/THEN
        assertThatThrownBy(() -> taskService.update(1L, new UpdateTaskRequest("x", null, null, null), owner))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    
}