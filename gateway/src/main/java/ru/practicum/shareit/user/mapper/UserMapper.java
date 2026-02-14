package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.server.user.model.User;

public class UserMapper {
    public static User dtoToJpa(ru.practicum.shareit.user.dto.UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }

    public static ru.practicum.shareit.user.dto.UserDto jpaToDto(User user) {
        return new ru.practicum.shareit.user.dto.UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }
}
