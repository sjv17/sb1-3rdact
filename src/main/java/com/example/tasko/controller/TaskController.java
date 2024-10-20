package com.example.tasko.controller;

import com.example.tasko.model.Task;
import com.example.tasko.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public String listTasks(Model model, 
                            @RequestParam(defaultValue = "0") int page, 
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(defaultValue = "dueDate") String sort,
                            @RequestParam(required = false) String filter) {
        Page<Task> taskPage = taskService.getTasksPaginated(PageRequest.of(page, size), sort, filter);
        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("filter", filter);
        return "tasks/list";
    }

    @GetMapping("/create")
    public String createTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@Valid @ModelAttribute Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "tasks/create";
        }
        Task createdTask = taskService.createTask(task);
        if (createdTask != null) {
            return "redirect:/tasks";
        } else {
            model.addAttribute("error", "Failed to create task. Please try again.");
            return "tasks/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editTaskForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            model.addAttribute("task", task);
            return "tasks/edit";
        }
        return "redirect:/tasks";
    }

    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, @Valid @ModelAttribute Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }
        task.setId(id);
        Task updatedTask = taskService.updateTask(task);
        if (updatedTask != null) {
            return "redirect:/tasks";
        } else {
            model.addAttribute("error", "Failed to update task. Please try again.");
            return "tasks/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/complete")
    @ResponseBody
    public ResponseEntity<String> completeTask(@PathVariable Long id) {
        Task completedTask = taskService.completeTask(id);
        if (completedTask != null) {
            return ResponseEntity.ok("Task completed successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to complete task");
        }
    }
}