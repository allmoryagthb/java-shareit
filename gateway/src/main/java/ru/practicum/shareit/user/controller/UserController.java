package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получить всех пользователей");
        return userClient.getAllUsersDto();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserById(@Positive @PathVariable(value = "userId") Long userId) {
        log.info("Получить пользователя с id = {}", userId);
        return userClient.getUserDtoById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody UserDto userDto) {
        log.info("Добавить нового пользователя");
        return userClient.addNewUser(userDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateUser(@Positive @PathVariable(value = "userId") Long userId, @RequestBody UserDto userDto) {
        log.info("Обновить пользователя с id = {}", userId);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@Positive @PathVariable(value = "userId") Long userId) {
        log.info("Удалить пользователя с id = {}", userId);
        userClient.deleteUserById(userId);
    }
}
