package org.mql.kanban.controller;

import java.util.List;
import org.mql.kanban.model.Project;
import org.mql.kanban.model.Task;
import org.mql.kanban.model.User;
import org.mql.kanban.repository.UserRepository;
import org.mql.kanban.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).orElse(null);

        if (user == null) {
            return "error"; 
        }

        Long userId = user.getId();
        List<Project> projects = projectService.getProjectsByUserId(userId);

        if (projects.isEmpty()) {
            System.out.println("Aucun projet trouvé pour l'utilisateur avec l'ID: " + userId);
        }

        model.addAttribute("projects", projects);
        return "index";
    }


    @GetMapping("add-project")
    public String addProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "add-project";
    }

    @PostMapping("add-project")
    public String saveProject(Project project) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).orElse(null);
        assert user != null;

        project.setUser(user);
        projectService.saveProject(project);
        return "redirect:/";
    }

    @GetMapping("/add-task/{projectId}")
    public String addTaskForm(@PathVariable("projectId") Long projectId, Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("projectId", projectId);
        return "add-task";
    }

    @PostMapping("/add-task/{projectId}")
    public String saveTask(@PathVariable("projectId") Long projectId, Task task) {
        projectService.addTaskToProject(projectId, task);
        return "redirect:/project/" + projectId;
    }

    @GetMapping("/project/{id}")
    public String viewProjectDetails(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).orElse(null);

        if (user == null) {
            System.out.println("Erreur : Utilisateur non trouvé.");
            return "redirect:/signin"; 
        }

        Project project = projectService.getProjectById(id);
        if (project == null) {
            System.out.println("Erreur : Projet non trouvé avec l'ID " + id);
            return "redirect:/"; 
        }

        if (!project.getUser().equals(user)) {
            System.out.println("Erreur : Accès non autorisé au projet ID " + id);
            return "redirect:/"; 
        }

        System.out.println("Projet trouvé : " + project.getName());
        project.getTasks().forEach(task ->
                System.out.println("Tâche : " + task.getTitle() + " (" + task.getStatus() + ")")
        );

        model.addAttribute("project", project);
        return "project-details";
    }
    
    
    @GetMapping("/edit-project/{id}")
    public String editProjectForm(@PathVariable("id") Long id, Model model) {
    	Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "edit-project"; // page d'update d'un projet
    }
    
    @PostMapping("/edit-project/{projectId}")
    public String editTask(@PathVariable("projectId") Long projectId, Project project) {
        projectService.editProject(projectId, project);
        return "redirect:/";
    }
    
    //delete
 // Suppression d'un projet
    @GetMapping("/delete-project/{id}")
    public String deleteProject(@PathVariable("id") Long id) {
        projectService.deleteProject(id);
        return "redirect:/"; // Rediriger vers la page d'accueil après suppression
    }
    
    
}