package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDtoFull> getUsersItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получить все предметы");
        return itemService.getUsersItemsDto(ownerId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoFull getItemById(@Positive @PathVariable(value = "itemId") Long id) {
        log.info("Получить предмет по id = {}", id);
        return itemService.getItemDtoById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> getItemsByName(@RequestParam(name = "text") String searchText) {
        log.info("Получить предметы, содержащие строку '{}'", searchText);
        return itemService.getAvailableItemsDtoByText(searchText);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Добавить новый предмет");
        return itemService.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @Positive @PathVariable(value = "itemId") Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Обновить предмет с id = {}", itemId);
        return itemService.updateItem(ownerId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItemById(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                               @Positive @PathVariable(value = "itemId") Long itemId) {
        log.info("Удалить предмет с id = {}", itemId);
        itemService.deleteItemById(ownerId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                 @Positive @PathVariable(value = "itemId") Long itemId,
                                 @RequestBody Comment comment) {
        log.info("Добавить комментарий");
        return itemService.addComment(ownerId, itemId, comment);
    }
}
