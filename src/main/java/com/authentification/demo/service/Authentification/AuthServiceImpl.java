package com.authentification.demo.service.Authentification;

import com.authentification.demo.dto.UserDto;
import com.authentification.demo.entity.Role;
import com.authentification.demo.entity.User;
import com.authentification.demo.exceptions.EmailAlreadyUsedException;
import com.authentification.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User saveUser(UserDto userDto) {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new EmailAlreadyUsedException("L'email est déjà utilisé.");
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        // Encodage du mot de passe
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Attribution d'un rôle par défaut
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
