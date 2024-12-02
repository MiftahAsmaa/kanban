package org.mql.kanban.controller;

import java.util.Map;

import org.mql.kanban.model.Task;
import org.mql.kanban.model.TaskStatus;
import org.mql.kanban.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks/list";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/tasks";
    }
    
    /*@PostMapping("/{taskId}/update-status")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable Long taskId, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");
        try {
            taskService.updateTaskStatus(taskId, TaskStatus.valueOf(newStatus));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }*/
    
    @PostMapping("/update-task-status")
    public String updateTaskStatus(@RequestParam Long taskId, @RequestParam String status) {
        System.out.println("Task ID: " + taskId + ", Status: " + status); // Pour vérifier les valeurs reçues
        Task task = taskService.getTaskById(taskId);
        if (task != null) {
            task.setStatus(TaskStatus.valueOf(status));
            taskService.saveTask(task);
            System.out.println("Task updated successfully");
        } else {
            System.out.println("Task not found");
        }
        return "redirect:/project/" + (task != null ? task.getProject().getId() : 0);
    }

    
}

