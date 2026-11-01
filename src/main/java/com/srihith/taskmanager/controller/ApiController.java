package com.srihith.taskmanager.controller;

import com.srihith.taskmanager.model.Task;
import com.srihith.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class ApiController {
    private final TaskService tasks;
    public ApiController(TaskService tasks) { this.tasks = tasks; }

    @GetMapping public List<Task> all() { return tasks.findAll(); }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody Task task) { return tasks.create(task); }
    @PatchMapping("/{id}/toggle") public Task toggle(@PathVariable Long id) { return tasks.toggle(id); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { tasks.delete(id); }
}
