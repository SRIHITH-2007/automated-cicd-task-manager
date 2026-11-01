package com.srihith.taskmanager.controller;

import com.srihith.taskmanager.model.Task;
import com.srihith.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {
    private final TaskService tasks;
    public WebController(TaskService tasks) { this.tasks = tasks; }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tasks", tasks.findAll());
        model.addAttribute("task", new Task());
        model.addAttribute("total", tasks.countAll());
        model.addAttribute("completed", tasks.countCompleted());
        return "index";
    }

    @PostMapping("/tasks")
    public String create(@Valid @ModelAttribute("task") Task task, BindingResult result,
                         Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("tasks", tasks.findAll());
            model.addAttribute("total", tasks.countAll());
            model.addAttribute("completed", tasks.countCompleted());
            return "index";
        }
        tasks.create(task);
        redirect.addFlashAttribute("message", "Task added successfully.");
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes redirect) {
        tasks.toggle(id);
        redirect.addFlashAttribute("message", "Task status updated.");
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        tasks.delete(id);
        redirect.addFlashAttribute("message", "Task deleted.");
        return "redirect:/";
    }
}
