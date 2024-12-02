package org.mql.kanban.service;

import java.util.List;
import java.util.Optional;

import org.mql.kanban.model.Task;
import org.mql.kanban.model.TaskStatus;
import org.mql.kanban.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /*public void updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setStatus(status);
        taskRepository.save(task);
    }*/
    
    public void updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(newStatus);
            taskRepository.save(task);
        } else {
            throw new RuntimeException("Tâche introuvable");
        }
    }
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

}
