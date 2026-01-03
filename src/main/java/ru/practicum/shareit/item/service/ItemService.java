package ru.practicum.shareit.item.service;

import jakarta.validation.constraints.Positive;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Collection<Item> getAllItems();

    Collection<ItemDto> getAllItemsDto();

    Collection<ItemDto> getUsersItems(Long ownerId);

    Item getItemById(Long id);

    Collection<Item> getItemsBySearchText(@Positive String searchText);

    ItemDto getItemDtoById(Long id);

    Collection<ItemDto> getItemsDtoBySearchText(@Positive String searchText);

    ItemDto addItem(Long ownerId, ItemDto itemDto);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);

    ItemDto deleteItemById(Long id);
}
