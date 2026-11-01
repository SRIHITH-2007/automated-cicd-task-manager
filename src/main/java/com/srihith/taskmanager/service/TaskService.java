package com.srihith.taskmanager.service;

import com.srihith.taskmanager.model.Task;
import com.srihith.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository repository;
    public TaskService(TaskRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<Task> findAll() { return repository.findAllByOrderByCreatedAtDesc(); }

    public Task create(Task task) {
        task.setId(null);
        task.setCompleted(false);
        return repository.save(task);
    }

    public Task toggle(Long id) {
        Task task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setCompleted(!task.isCompleted());
        return repository.save(task);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new TaskNotFoundException(id);
        repository.deleteById(id);
    }

    public long countAll() { return repository.count(); }
    public long countCompleted() { return repository.findAll().stream().filter(Task::isCompleted).count(); }
}
