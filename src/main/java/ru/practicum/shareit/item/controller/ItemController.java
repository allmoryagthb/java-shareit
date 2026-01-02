package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Item> getUsersItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getUsersItems(ownerId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@Positive @PathVariable(value = "itemId") Long id) {
        return itemService.getItemDtoById(id);
    }

    @GetMapping("/{itemName}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemByName(@Positive @PathVariable(value = "itemName") String itemName) {
        return itemService.getItemDtoByName(itemName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @Valid @RequestBody ItemDto itemDto) {
        return itemService.addItem(ownerId, itemDto);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @Valid @RequestBody ItemDto itemDto) {
        return itemService.updateItem(ownerId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto deleteItemById(@Positive @PathVariable(value = "itemId") Long itemId) {
        return itemService.deleteItemById(itemId);
    }
}
