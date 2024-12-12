package org.mql.kanban.controller;

import org.mql.kanban.model.User;
import org.mql.kanban.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signin")
    public String loginPage() {
        return "login";
    }

    // Méthode pour afficher le formulaire d'ajout d'utilisateur
    @GetMapping("/signup")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "addUser"; // Le nom de la vue pour le formulaire d'ajout d'utilisateur
    }

    // Méthode pour traiter l'ajout d'utilisateur
    @PostMapping("/signup")
    public String addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/signin"; // Redirige vers la liste des utilisateurs après ajout
    }
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll(); // Récupérer tous les utilisateurs
        model.addAttribute("users", users); // Ajouter la liste au modèle
        return "userList"; // Nom du fichier Thymeleaf sans l'extension .html
    }

}
