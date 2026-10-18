package com.example.task_tracker.service;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.TaskStatus;
import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.TaskDtos.CreateTaskRequest;
import com.example.task_tracker.dto.TaskDtos.TaskResponse;
import com.example.task_tracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


import static com.example.task_tracker.support.Users.withId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskServiceCreateTest {

    private TaskService service;
    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(TaskRepository.class);
        service = new TaskService(repository);
    }

    @Test
    void create_setsOwnerAndDefaultsStatus() {
        // GIVEN
        User owner = withId(42L);
        CreateTaskRequest req = new CreateTaskRequest("Write README", "docs", null);

        when(repository.save(any(Task.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        TaskResponse res = service.create(req, owner);

        // THEN
        assertThat(res).isNotNull();
        assertThat(res.title()).isEqualTo("Write README");
        assertThat(res.status()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void listForUser_returnsEmptyList_whenUserHasNoTasks() {
        // GIVEN
        User owner = withId(42L);
        when(repository.findAllByOwnerId(42L)).thenReturn(List.of());

        // WHEN
        List<TaskResponse> result = service.listForUser(owner);

        // THEN
        assertThat(result).isEmpty();
    }
}