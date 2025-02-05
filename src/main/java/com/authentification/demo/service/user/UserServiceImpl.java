package com.authentification.demo.service.user;

import com.authentification.demo.entity.User;
import com.authentification.demo.dto.UserDto;
import com.authentification.demo.entity.Role;
import com.authentification.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsersExcept(Long userId) {
        return userRepository.findAllByIdNot(userId); // Supposons que vous avez une méthode dans le repository pour
                                                      // exclure un utilisateur
    }

    @Override
    public List<User> searchUsersExcept(String keyword, Long userId) {
        return userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAndIdNot(keyword, keyword,
                keyword, userId);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
public void updateUser(Long id, UserDto userDto) {
    // Recherche de l'utilisateur par son ID
    Optional<User> existingUser = userRepository.findById(id);

    if (existingUser.isPresent()) {
        User user = existingUser.get();

        // Mise à jour des informations de l'utilisateur
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        // Vérification du rôle avant de le traiter
        String roleString = userDto.getRole();
        if (roleString != null && !roleString.isEmpty()) {
            try {
                // Conversion du rôle en Enum Role et mise à jour
                Role role = Role.valueOf(roleString.toUpperCase());
                user.setRole(role);
            } catch (IllegalArgumentException e) {
                throw new InvalidRoleException("Rôle invalide : " + roleString);
            }
        } else {
            throw new InvalidRoleException("Le rôle ne peut pas être null ou vide.");
        }

        // Sauvegarde de l'utilisateur mis à jour
        userRepository.save(user);
    } else {
        throw new UserNotFoundException("Utilisateur non trouvé pour l'ID : " + id);
    }
}

    
    // Exception personnalisée pour le rôle invalide
    public class InvalidRoleException extends RuntimeException {
        public InvalidRoleException(String message) {
            super(message);
        }
    }

    // Exception personnalisée pour utilisateur non trouvé
    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        userRepository.delete(user); // Supprimer directement l'utilisateur
    }

    @Override
    public List<User> searchUsers(String keyword) {
        return userRepository.findByFirstNameContainingOrEmailContaining(keyword, keyword);
    }

}
