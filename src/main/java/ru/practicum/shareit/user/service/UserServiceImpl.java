package ru.practicum.shareit.user.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUserById(@Positive Long userId) {
        return userStorage.getUserById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    @Override
    public UserDto getUserDtoById(@Positive Long userId) {
        return UserMapper.jpaToDto(getUserById(userId));
    }

    @Override
    public User addNewUser(@Valid @RequestBody UserDto userDto) {
        checkEmailExists(userDto.getEmail());
        return userStorage.addUser(UserMapper.dtoToJpa(userDto));
    }

    @Override
    public User updateUser(Long id, UserDto userDto) {
        checkEmailExists(userDto.getEmail());
        User userToUpd = getUserById(id);
        if (Objects.nonNull(userDto.getName()) && !userToUpd.getName().equals(userDto.getName()))
            userToUpd.setName(userDto.getName());
        if (Objects.nonNull(userDto.getEmail()) && !userToUpd.getEmail().equals(userDto.getEmail()))
            userToUpd.setEmail(userDto.getEmail());
        return userToUpd;
    }

    @Override
    public User deleteUserById(Long id) {
        return userStorage.deleteUserById(id);
    }

    private void checkEmailExists(String email) {
        if (userStorage.getAllUsers().stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(email)))
            throw new RuntimeException("Пользователь с таким email уже существует");
    }
}
