package com.authentification.demo.repository;

import com.authentification.demo.entity.User;

import com.authentification.demo.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);

    User findByEmail(String email);

    List<User> findByFirstNameContainingOrEmailContaining(String firstName, String email);

    List<User> findAllByIdNot(Long id);

    List<User> findByFirstNameContainingOrLastNameContainingOrEmailContainingAndIdNot(
            String firstName,
            String lastName,
            String email,
            Long id);
}
