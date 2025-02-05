package com.authentification.demo.configuration.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    // Utilisation d'un Bean pour l'encodeur de mot de passe (BCryptPasswordEncoder)
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuration du filtre de sécurité pour gérer l'authentification et les autorisations
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Désactivation CSRF, pas nécessaire pour les API REST ou si vous gérez les tokens séparément
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/register/**", "/index").permitAll() // Permet l'accès public à la page d'inscription et d'accueil
                .requestMatchers("/adminDashboard", "/admin/**").hasRole("ADMIN") // Accès réservé aux utilisateurs admin
                .requestMatchers("/userDashboard", "/user/**").hasRole("USER") // Accès réservé aux utilisateurs
                .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
            )
            .formLogin(form -> form
                .loginPage("/login") // Page de login personnalisée
                .loginProcessingUrl("/login") // URL de traitement de la connexion
                .successHandler(successHandler) // Handler personnalisé après la connexion réussie
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL pour se déconnecter
                .logoutSuccessUrl("/login?logout") // Redirection après déconnexion
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Crée la session si nécessaire
                .maximumSessions(1)  // Limite le nombre de sessions simultanées à 1 par utilisateur
                .expiredUrl("/login?expired=true")  // Redirige vers la page de connexion si la session expire
                .maxSessionsPreventsLogin(true) // Empêche l'utilisateur de se connecter à une nouvelle session si une autre est déjà active
            );

        return http.build();
    }

    // Configuration pour l'authentification globale avec un service personnalisé pour les utilisateurs
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)  // Utilisation de UserDetailsService personnalisé
            .passwordEncoder(passwordEncoder());  // Utilisation de l'encodeur de mot de passe
    }
}
