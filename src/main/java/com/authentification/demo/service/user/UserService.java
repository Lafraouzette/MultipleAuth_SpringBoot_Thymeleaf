package com.authentification.demo.service.user;

import com.authentification.demo.dto.UserDto;
import com.authentification.demo.entity.User;
import java.util.List;

public interface UserService {
    User findByEmail(String email);
    List<User> getAllUsersExcept(Long userId);
    List<User> searchUsersExcept(String keyword, Long userId);
    User getUserById(Long id);
    void updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    List<User> searchUsers(String keyword);
}
