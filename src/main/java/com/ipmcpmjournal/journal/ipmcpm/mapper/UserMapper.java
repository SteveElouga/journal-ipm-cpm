package com.ipmcpmjournal.journal.ipmcpm.mapper;

import com.ipmcpmjournal.journal.ipmcpm.dto.UserDto;
import com.ipmcpmjournal.journal.ipmcpm.model.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhoto(),
                user.getAdresse(),
                user.getPassword(),
                user.getRole()
        );
    }

    public static User mapToUser(UserDto userDto){
        return new User(
                userDto.getId(),
                userDto.getFirstname(),
                userDto.getLastname(),
                userDto.getEmail(),
                userDto.getPhoto(),
                userDto.getAdresse(),
                userDto.getPassword(),
                userDto.getRole()
        );
    }
}
