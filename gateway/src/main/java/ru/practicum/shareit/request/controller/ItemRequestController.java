package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUsersItemRequests(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получить все заявки пользователя с id = '{}'", ownerId);
        return itemRequestClient.getUsersItemRequests(ownerId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUsersItemRequests() {
        log.info("Получить все заявки пользователей");
        return itemRequestClient.getAllUsersItemRequests();
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUsersItemRequestById(@Positive @PathVariable(value = "requestId") Long requestId) {
        log.info("Получить заявку с id = '{}'", requestId);
        return itemRequestClient.getUsersItemRequestById(requestId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addUsersItemRequests(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                       @Valid @RequestBody ItemRequestDtoInput ItemRequestDtoInput) {
        log.info("Добавить заявку");
        return itemRequestClient.addUsersItemRequest(ownerId, ItemRequestDtoInput);
    }
}
