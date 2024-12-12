package org.mql.kanban.controller;

import org.mql.kanban.model.Task;
import org.mql.kanban.model.TaskStatus;
import org.mql.kanban.repository.TaskRepository;
import org.mql.kanban.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;

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
    
    @DeleteMapping("/supp/{taskId}")
    @ResponseBody
    public String deleteTask(@PathVariable Long taskId) {
        try {
            taskRepository.deleteById(taskId); // Supprimer seulement la tâche spécifique
            return "Tâche supprimée avec succès!";
        } catch (Exception e) {
            return "Erreur lors de la suppression de la tâche.";
        }
    }


    @PostMapping("/{taskId}/{status}")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long taskId, @PathVariable String status) {
        try {
            Task task = taskService.getTaskById(taskId);
            if (task == null) {
                return ResponseEntity.badRequest().body("Task not found");
            }

            TaskStatus newStatus = TaskStatus.valueOf(status.toUpperCase());
            task.setStatus(newStatus);
            taskService.saveTask(task);

            return ResponseEntity.ok("Task updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value: " + status);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
    
   


}
