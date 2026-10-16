package com.example.task_tracker.repository;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByOwnerId(Long ownerId);

    Optional<Task> findByIdAndOwnerId(Long id, Long ownerId);

    List<Task> findAllByOwnerIdAndStatus(Long ownerId, TaskStatus status);

    List<Task> findAllByOwnerIdAndDueDateBefore(Long ownerId, LocalDate date);

    boolean existsByIdAndOwnerId(Long id, Long ownerId);

    long countByOwnerIdAndStatus(Long ownerId, TaskStatus status);

        // Custom JPQL queries (new in 4.2.2)

    @Query("SELECT t FROM Task t WHERE t.owner.id = :ownerId AND t.status = :status")
    List<Task> findByOwnerAndStatus(@Param("ownerId") Long ownerId,
                                    @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t JOIN FETCH t.owner WHERE t.owner.id = :ownerId")
    List<Task> findAllWithOwner(@Param("ownerId") Long ownerId);

    @Modifying
    @Query("UPDATE Task t SET t.status = :status WHERE t.id = :id AND t.owner.id = :ownerId")
    int updateStatus(@Param("id") Long id,
                     @Param("status") TaskStatus status,
                     @Param("ownerId") Long ownerId);

    @Query(value = "SELECT * FROM tasks WHERE owner_id = :ownerId AND due_date < CURRENT_DATE",
           nativeQuery = true)
    List<Task> findOverdueNative(@Param("ownerId") Long ownerId);
    
}