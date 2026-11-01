package com.srihith.taskmanager.repository;

import com.srihith.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByOrderByCreatedAtDesc();
}
