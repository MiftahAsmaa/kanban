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
            // Gérer le cas où l'utilisateur n'est pas trouvé (peut-être rediriger vers une page d'erreur)
            return "error"; // Exemple : renvoyer une page d'erreur
        }

        Long userId = user.getId();

        // Récupérer les projets de l'utilisateur
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
        Long userId = user.getId();

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
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).orElse(null);

        if (user == null) {
            System.out.println("Erreur : Utilisateur non trouvé.");
            return "redirect:/signin"; // Rediriger vers la page de connexion si l'utilisateur n'est pas trouvé
        }

        // Récupérer le projet par ID
        Project project = projectService.getProjectById(id);
        if (project == null) {
            System.out.println("Erreur : Projet non trouvé avec l'ID " + id);
            return "redirect:/"; // Rediriger vers une page d'accueil ou d'erreur
        }

        // Vérifier si l'utilisateur est propriétaire du projet
        if (!project.getUser().equals(user)) {
            System.out.println("Erreur : Accès non autorisé au projet ID " + id);
            return "redirect:/"; // Rediriger vers une page d'erreur
        }

        // Afficher les détails du projet si l'utilisateur est autorisé
        System.out.println("Projet trouvé : " + project.getName());
        project.getTasks().forEach(task ->
                System.out.println("Tâche : " + task.getTitle() + " (" + task.getStatus() + ")")
        );

        model.addAttribute("project", project);
        return "project-details"; // Vue pour afficher les détails du projet
    }
}