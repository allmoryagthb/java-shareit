package ru.practicum.shareit.user.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User getUserById(@Positive Long userId);

    UserDto getUserDtoById(@Positive Long userId);

    User addNewUser(@Valid UserDto userDto);

    User updateUser(Long id, @Valid UserDto userDto);

    User deleteUserById(@Positive Long id);
}
