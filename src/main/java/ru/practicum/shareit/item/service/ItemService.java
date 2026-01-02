package ru.practicum.shareit.item.service;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public Collection<Item> getAllItems() {
        return itemStorage.getAllItems();
    }

    public Collection<Item> getUsersItems(Long ownerId) {
        return itemStorage.getUsersItems(ownerId);
    }

    public Item getItemById(Long id) {
        return itemStorage.getItemById(id)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с таким id не найден"));
    }

    public Item getItemByName(@Positive String itemName) {
        return itemStorage.getItemByName(itemName)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с таким именем не найден"));
    }

    public ItemDto getItemDtoById(Long id) {
        return ItemMapper.jpaToDto(getItemById(id));
    }

    public ItemDto getItemDtoByName(@Positive String itemName) {
        return ItemMapper.jpaToDto(getItemByName(itemName));
    }

    public ItemDto addItem(Long ownerId, ItemDto itemDto) {
        Item newItem = ItemMapper.dtoToJpa(itemDto);
        newItem.setOwner(userStorage.getUserById(ownerId));
        return ItemMapper.jpaToDto(itemStorage.addItem(newItem));
    }

    public ItemDto updateItem(Long ownerId, ItemDto itemDto) {
        Item itemToUpdate = getItemById(itemDto.getId());
        if (!itemToUpdate.getOwner().getId().equals(ownerId))
            throw new ValidationException("Пользователь не является владельцем предмета");
        if (!Objects.isNull(itemDto.getName()))
            itemToUpdate.setName(itemDto.getName());
        if (!Objects.isNull(itemDto.getDescription()))
            itemToUpdate.setDescription(itemDto.getDescription());
        if (!Objects.isNull(itemDto.getIsAvailableStatus()))
            itemToUpdate.setIsAvailableStatus(itemDto.getIsAvailableStatus());
        return ItemMapper.jpaToDto(itemStorage.updateItem(itemToUpdate));
    }

    public ItemDto deleteItemById(Long id) {
        getItemById(id);
        return ItemMapper.jpaToDto(itemStorage.deleteItem(id));
    }
}
