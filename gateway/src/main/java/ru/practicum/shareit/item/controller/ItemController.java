package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collections;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUsersItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получить все предметы");
        return itemClient.getUsersItems(ownerId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @Positive @PathVariable(value = "itemId") Long id) {
        log.info("Получить предмет по id = {}", id);
        return itemClient.getItemById(id, userId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @PathVariable(value = "itemId") Long itemId,
                                             @RequestBody CommentDto commentDto) {
        log.info("Добавить комментарий");
        return itemClient.addComment(ownerId, itemId, commentDto);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemsByName(@NotBlank @RequestParam(name = "text") String searchText) {
        log.info("Получить предметы, содержащие строку '{}'", searchText);
        if (searchText.isBlank())
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        return itemClient.getAvailableItemsDtoByText(searchText);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addNewItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("Добавить новый предмет");
        return itemClient.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @PathVariable(value = "itemId") Long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Обновить предмет с id = {}", itemId);
        return itemClient.updateItem(ownerId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteItemById(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                 @PathVariable(value = "itemId") Long itemId) {
        log.info("Удалить предмет с id = {}", itemId);
        return itemClient.deleteItemById(ownerId, itemId);
    }
}
