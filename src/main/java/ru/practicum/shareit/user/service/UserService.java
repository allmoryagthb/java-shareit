package ru.practicum.shareit.user.service;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
       return userStorage.getAllUsers();
    }

    public UserDto getUserById(@Positive Long userId) {
        return UserMapper.jpaToDto(userStorage.getUserById(userId));
    }
}
