package ru.practicum.shareit.user.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    @Transactional(readOnly = true)
    Collection<UserDto> getAllUsersDto();

    @Transactional(readOnly = true)
    UserDto getUserDtoById(@Positive Long userId);

    @Transactional
    UserDto addNewUser(@Valid UserDto userDto);

    @Transactional
    UserDto updateUser(@Positive Long id, @Valid UserDto userDto);

    @Transactional
    void deleteUserById(@Positive Long id);
}
