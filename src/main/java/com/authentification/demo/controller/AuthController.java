package com.authentification.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.authentification.demo.dto.UserDto;
import com.authentification.demo.entity.User;
import com.authentification.demo.exceptions.EmailAlreadyUsedException;
import com.authentification.demo.service.Authentification.AuthService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private AuthService userService;

    public AuthController(AuthService userService) {
        this.userService = userService;
    }

    // handler method to handle home page request
    @GetMapping("/index")
    public String home() {
        return "index";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login(Authentication authentication) {
        // Vérifier si une authentification existe et est valide
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            // Vérifier le rôle de l'utilisateur
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

            // Rediriger vers le dashboard approprié
            return isAdmin ? "redirect:/adminDashboard" : "redirect:/userDashboard";
        }

        // Afficher le formulaire de login
        return "auth/login";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "auth/register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registerUser(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {
        try {
            userService.saveUser(userDto);
            return "redirect:/login?success"; // Rediriger vers la page de login après inscription réussie
        } catch (EmailAlreadyUsedException e) {
            model.addAttribute("errorMessage", e.getMessage()); // Ajouter le message d'erreur
            return "auth/register"; // Rester sur la page d'inscription
        }
    }

    // Handler pour afficher le tableau de bord admin
    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("message", "Bienvenue sur le tableau de bord Admin !");
        return "admin/dashboard"; // Page Thymeleaf : templates/admin/dashboard.html
    }

    // Handler pour afficher le tableau de bord utilisateur
    @GetMapping("/userDashboard")
    public String userDashboard(Model model) {
        model.addAttribute("message", "Bienvenue sur le tableau de bord Utilisateur !");
        return "user/dashboard"; // Page Thymeleaf : templates/user/dashboard.html
    }
}
