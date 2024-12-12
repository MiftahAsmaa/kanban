package org.mql.kanban.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.mql.kanban.model.Task;
import org.mql.kanban.model.TaskStatus;
import org.mql.kanban.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
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
    
    public void deleteTask(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();

            // Vérifiez si la tâche est déjà associée à un projet et gérez cela si nécessaire
            if (task.getProject() != null) {
                task.setProject(null); // Dissocier le projet avant la suppression, si nécessaire
            }

            // Supprimer la tâche
            taskRepository.delete(task);
            System.out.println("Tâche supprimée avec succès.");
        } else {
            System.out.println("Aucune tâche trouvée avec l'ID: " + taskId);
            throw new RuntimeException("Tâche non trouvée pour l'ID : " + taskId);  // Lever une exception explicite
        }
    }




}
