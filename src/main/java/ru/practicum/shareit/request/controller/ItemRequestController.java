package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public Collection<ItemRequestDtoOutput> getUsersItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получить все предметы");
        return itemRequestService.getUsersItemRequests(ownerId);
    }
}
