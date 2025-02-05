# Projet Spring Boot : Authentification Multiple avec Thymeleaf

## Objectif
- Mise en œuvre de Spring Security
- Authentification et Autorisation
- Utilisation de fragments de composants dans le frontend
- API REST simple pour la gestion des utilisateurs par un administrateur
- Gérer les sessions : configurer la durée de la session, limiter le nombre de sessions simultanées . . . ; Tous cela dans le fichier app.yml + SecurityConfig 
## Outils Utilisés
- Java 17
- Spring Boot 3.4.2
- Thymeleaf
- Dépendances complètes disponibles dans le fichier pom.xml

## Architecture du Projet
Architecture en 3 couches avec modèle architectural MVC

### Structure Backend
```
│   DemoApplication.java
│
├───configuration
│   └───securityConfig
│           CustomAuthenticationSuccessHandler.java
│           SpringSecurity.java
│
├───controller
│       AuthController.java
│       UserController.java
│
├───dto
│       UserDto.java
│
├───entity
│       Role.java
│       User.java
│
├───repository
│       UserRepository.java
│
└───service
    │   CustomUserDetailsService.java
    │
    ├───Authentification
    │       AuthService.java
    │       AuthServiceImpl.java
    │
    └───user
            UserService.java
            UserServiceImpl.java
```

### Structure Frontend
```
│   index.html
│
├───admin
│   │   dashboard.html
│   │
│   └───user
│           editUser.html
│           users.html
│
├───auth
│       login.html
│       register.html
│
├───fragments
│   ├───admin
│   ├───generale
│   │       footer.html
│   │       header.html
│   │
│   └───user
└───user
    │   dashboard.html
    │
    ├───product
    └───store
```

## Rôles des Fichiers Clés de Configuration de Sécurité

### 1. CustomAuthenticationSuccessHandler.java
**Rôle :** 
- Personnalise le comportement après une authentification réussie
- Gère la redirection des utilisateurs vers différentes pages en fonction de leurs rôles
- Permet une logique personnalisée post-connexion (journalisation, mise à jour de session, etc.)

### 2. SpringSecurity.java
**Rôle :**
- Configuration principale de la sécurité Spring
- Définit les règles de sécurité globales
- Configure :
  - Chemins d'accès protégés/publics
  - Mécanismes d'authentification
  - Configuration des filtres de sécurité
  - Gestion des autorisations par rôle
  - Paramètres de connexion/déconnexion

### 3. CustomUserDetailsService.java
**Rôle :**
- Implémente l'interface UserDetailsService de Spring Security
- Charge les détails de l'utilisateur depuis la source de données (base de données)
- Convertit les utilisateurs de l'application en objets UserDetails compréhensibles par Spring Security
- Gère l'authentification et la récupération des informations utilisateur
- Fournit les détails nécessaires pour l'authentification et l'autorisation

## Points Clés
- Personnalisation complète du processus d'authentification
- Séparation claire des responsabilités
- Flexibilité dans la gestion des utilisateurs et des rôles
- Sécurisation robuste des ressources de l'application