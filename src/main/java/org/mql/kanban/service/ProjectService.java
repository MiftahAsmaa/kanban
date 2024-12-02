package org.mql.kanban.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.mql.kanban.model.Project;
import org.mql.kanban.model.Task;
import org.mql.kanban.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        System.out.println("Nombre de projets trouvés : " + projects.size());
        return projects;
    }
    public Project getProjectById(int id) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {
            System.out.println("Projet trouvé : " + project.getName());
            System.out.println("Tâches associées : ");
            project.getTasks().forEach(task -> 
                System.out.println(" - " + task.getTitle() + " (" + task.getStatus() + ")")
            );
        } else {
            System.out.println("Aucun projet trouvé avec l'ID : " + id);
        }

        return project;
    }
/*
    public Project getProjectById(int id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElse(null);
    }
*/
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public void addTaskToProject(int projectId, Task task) {
        Project project = getProjectById(projectId);
        if (project != null) {
            task.setProject(project);
            project.getTasks().add(task);
            projectRepository.save(project);
        }
    }
}


