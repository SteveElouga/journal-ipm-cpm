package com.ipmcpmjournal.journal.ipmcpm.service;

import com.ipmcpmjournal.journal.ipmcpm.dto.UserDto;
import com.ipmcpmjournal.journal.ipmcpm.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long userId);

    UserDto getUserByEmail(String email);

    User getUserOAuthByEmail(String email);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long userId, UserDto updateUser);

    void deleteUser(Long userId);

    void activation(Map<String, String> activation);

    void modifierMotDePasse(Map<String, String> activation);

    void nouveauMotDePasse(Map<String, String> activation);
}
