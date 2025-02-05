package com.authentification.demo.service.Authentification;

import java.util.List;

import com.authentification.demo.dto.UserDto;
import com.authentification.demo.entity.User;

public interface AuthService {
    User saveUser(UserDto userDto);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
}
