package com.srihith.taskmanager;

import com.srihith.taskmanager.model.Task;
import com.srihith.taskmanager.repository.TaskRepository;
import com.srihith.taskmanager.service.TaskNotFoundException;
import com.srihith.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceTest {
    @Autowired TaskService service;
    @Autowired TaskRepository repository;

    @BeforeEach void clean() { repository.deleteAll(); }

    @Test void createsTaskAsIncomplete() {
        Task task = new Task();
        task.setTitle("Write CI workflow");
        Task saved = service.create(task);
        assertNotNull(saved.getId());
        assertFalse(saved.isCompleted());
    }

    @Test void togglesTaskCompletion() {
        Task task = new Task();
        task.setTitle("Build Docker image");
        Task saved = service.create(task);
        assertTrue(service.toggle(saved.getId()).isCompleted());
        assertFalse(service.toggle(saved.getId()).isCompleted());
    }

    @Test void deletesTask() {
        Task task = new Task();
        task.setTitle("Deploy app");
        Task saved = service.create(task);
        service.delete(saved.getId());
        assertEquals(0, service.countAll());
    }

    @Test void missingTaskThrowsNotFound() {
        assertThrows(TaskNotFoundException.class, () -> service.toggle(99999L));
    }
}
