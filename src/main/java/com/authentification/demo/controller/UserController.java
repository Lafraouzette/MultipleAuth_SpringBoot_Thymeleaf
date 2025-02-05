package com.authentification.demo.controller;

import com.authentification.demo.dto.UserDto;
import com.authentification.demo.entity.Role;
import com.authentification.demo.entity.User;
import com.authentification.demo.service.user.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String listUsers(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        // Récupérer l'utilisateur connecté
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();// getname renvoie
                                                                                                  // lidentifiant par le
                                                                                                  // quel lutilisateur
                                                                                                  // est cnct qui est
                                                                                                  // generalement email
        User currentUser = userService.findByEmail(currentUsername); // tu lutilise ici

        // Récupérer tous les utilisateurs sauf celui connecté
        List<User> users = userService.getAllUsersExcept(currentUser.getId());

        // Filtrer par mot-clé si nécessaire
        if (keyword != null && !keyword.isEmpty()) {
            users = userService.searchUsersExcept(keyword, currentUser.getId());
        }

        // Ajouter les données au modèle
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser); // Optionnel : si vous avez besoin de l'utilisateur connecté
                                                        // dans le template
        model.addAttribute("keyword", keyword);

        return "admin/user/users"; // Le nom du template Thymeleaf
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values()); // Passe la liste des rôles à la vue
        return "admin/user/editUser";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserDto userDto) {
        // Mettre à jour l'utilisateur via le service
        userService.updateUser(id, userDto);

        // Redirection vers la liste des utilisateurs après modification
        return "redirect:/users";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        System.out.println("Suppression de l'utilisateur avec l'ID : " + id);
        userService.deleteUser(id);
        return "redirect:/users";
    }

}
