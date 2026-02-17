package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    @Autowired
    private ItemRequestService itemRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDtoOutput> getUsersItemRequests(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получить все заявки пользователя с id = '{}'", ownerId);
        return itemRequestService.getUsersItemRequests(ownerId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDtoOutput> getAllUsersItemRequests() {
        log.info("Получить все заявки пользователей");
        return itemRequestService.getAllUsersItemRequests();
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoOutput getUsersItemRequestById(@Positive @PathVariable(value = "requestId") Long requestId) {
        log.info("Получить заявку с id = '{}'", requestId);
        return itemRequestService.getUsersItemRequestById(requestId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoOutput addUsersItemRequests(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                     @Valid @RequestBody ItemRequestDtoInput itemRequestDtoInput) {
        log.info("Добавить заявку");
        return itemRequestService.addUsersItemRequest(ownerId, itemRequestDtoInput);
    }


}
