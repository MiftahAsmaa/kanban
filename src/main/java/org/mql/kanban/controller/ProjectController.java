package org.mql.kanban.controller;

import java.util.List;

import org.mql.kanban.model.Project;
import org.mql.kanban.model.Task;
import org.mql.kanban.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
    	 List<Project> projects = projectService.getAllProjects();
         if (projects.isEmpty()) {
             System.out.println("Aucun projet trouvé dans la base de données.");
         }
         model.addAttribute("projects", projects);
        return "index";
    }
    

   /* @GetMapping("/project/{id}")
    public String viewProjectDetails(@PathVariable("id") int id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "project-details"; // page de détails du projet
    }*/

    @GetMapping("add-project")
    public String addProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "add-project"; // page d'ajout de projet
    }

    @PostMapping("add-project")
    public String saveProject(Project project) {
        projectService.saveProject(project);
        return "redirect:/";
    }

    @GetMapping("/add-task/{projectId}")
    public String addTaskForm(@PathVariable("projectId") Long projectId, Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("projectId", projectId);
        return "add-task"; // page d'ajout de tâche
    }

    @PostMapping("/add-task/{projectId}")
    public String saveTask(@PathVariable("projectId") int projectId, Task task) {
        projectService.addTaskToProject(projectId, task);
        return "redirect:/project/" + projectId;
    }
    @GetMapping("/project/{id}")
    public String viewProjectDetails(@PathVariable("id") int id, Model model) {
        Project project = projectService.getProjectById(id);

        if (project == null) {
            System.out.println("Erreur : Projet non trouvé avec l'ID " + id);
            return "redirect:/"; // Redirige vers la page d'accueil si le projet n'existe pas
        }

        System.out.println("Projet trouvé : " + project.getName());
        project.getTasks().forEach(task -> 
            System.out.println("Tâche : " + task.getTitle() + " (" + task.getStatus() + ")")
        );

        model.addAttribute("project", project);
        return "project-details";
    }



}
