package com.authentification.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.authentification.demo.dto.UserDto;
import com.authentification.demo.entity.User;
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
    public String login() {
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
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "auth/register";
        }

        userService.saveUser(userDto);
        return "redirect:/users";
    }

    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("message", "Bienvenue sur le tableau de bord Admin !");
        return "admin/dashboard"; // Fichier Thymeleaf : templates/admin/dashboard.html
    }


    @GetMapping("/userDashboard")
    public String userDashboard(Model model) {
        model.addAttribute("message", "Bienvenue sur le tableau de bord Utilisateur !");
        return "user/dashboard"; 
    }
   
}