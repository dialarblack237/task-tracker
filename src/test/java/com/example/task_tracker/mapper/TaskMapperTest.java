package com.example.task_tracker.mapper;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.TaskStatus;
import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.TaskDtos.CreateTaskRequest;
import com.example.task_tracker.dto.TaskDtos.TaskResponse;
import com.example.task_tracker.dto.TaskDtos.UpdateTaskRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.example.task_tracker.support.Users.withId;
import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    @Test
    void toEntity_mapsFields_setsOwner_andDefaultsStatusTodo() {
        User owner = withId(42L);
        CreateTaskRequest req = new CreateTaskRequest("T", "d", LocalDate.of(2030, 1, 1));

        Task entity = TaskMapper.toEntity(req, owner);

        assertThat(entity.getTitle()).isEqualTo("T");
        assertThat(entity.getDescription()).isEqualTo("d");
        assertThat(entity.getDueDate()).isEqualTo(LocalDate.of(2030, 1, 1));
        assertThat(entity.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(entity.getOwner()).isSameAs(owner);
    }

    @Test
    void toResponse_mapsAllFields() {
        Task t = new Task();
        t.setTitle("T");
        t.setDescription("d");
        t.setStatus(TaskStatus.DONE);
        t.setDueDate(LocalDate.of(2030, 1, 1));

        TaskResponse res = TaskMapper.toResponse(t);

        assertThat(res.title()).isEqualTo("T");
        assertThat(res.description()).isEqualTo("d");
        assertThat(res.status()).isEqualTo(TaskStatus.DONE);
        assertThat(res.dueDate()).isEqualTo(LocalDate.of(2030, 1, 1));
        // createdAt / updatedAt are JPA-auditing fields; null pre-save.
        assertThat(res.createdAt()).isNull();
        assertThat(res.updatedAt()).isNull();
    }

    @Test
    void applyUpdate_overwritesAllFields() {
        Task task = new Task();
        task.setTitle("Old");
        task.setDescription("old desc");
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(LocalDate.of(2027, 1, 1));

        UpdateTaskRequest req = new UpdateTaskRequest(
                "New", "new desc", TaskStatus.DONE, LocalDate.of(2030, 1, 1));

        TaskMapper.applyUpdate(task, req);

        assertThat(task.getTitle()).isEqualTo("New");
        assertThat(task.getDescription()).isEqualTo("new desc");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.DONE);
        assertThat(task.getDueDate()).isEqualTo(LocalDate.of(2030, 1, 1));
    }

    @Test
    void applyUpdate_doesNotTouchOwnerOrId() {
        User owner = withId(42L);
        Task task = new Task();
        task.setTitle("Old");
        task.setOwner(owner);
        task.setStatus(TaskStatus.TODO);

        UpdateTaskRequest req = new UpdateTaskRequest(
                "New", "desc", TaskStatus.DONE, null);

        TaskMapper.applyUpdate(task, req);

        assertThat(task.getOwner()).isSameAs(owner);
    }
}