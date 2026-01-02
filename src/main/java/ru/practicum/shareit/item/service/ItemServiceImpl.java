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
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Collection<Item> getAllItems() {
        return itemStorage.getAllItems();
    }

    @Override
    public Collection<ItemDto> getAllItemsDto() {
        return getAllItems().stream().map(ItemMapper::jpaToDto).toList();
    }

    @Override
    public Collection<ItemDto> getUsersItems(Long ownerId) {
        return itemStorage.getUsersItems(ownerId);
    }

    @Override
    public Item getItemById(Long id) {
        return itemStorage.getItemById(id)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с таким id не найден"));
    }

    @Override
    public Collection<Item> getItemsBySearchText(@Positive String searchText) {
        return itemStorage.getAvailableItemByName(searchText);
    }

    @Override
    public ItemDto getItemDtoById(Long id) {
        return ItemMapper.jpaToDto(getItemById(id));
    }

    @Override
    public Collection<ItemDto> getItemsDtoBySearchText(@Positive String searchText) {
        return getItemsBySearchText(searchText).stream().map(ItemMapper::jpaToDto).toList();
    }

    @Override
    public ItemDto addItem(Long ownerId, ItemDto itemDto) {
        Item newItem = ItemMapper.dtoToJpa(itemDto);
        newItem.setOwner(userService.getUserById(ownerId));
        return ItemMapper.jpaToDto(itemStorage.addItem(newItem));
    }

    @Override
    public ItemDto updateItem(Long ownerId, ItemDto itemDto) {
        Item itemToUpdate = getItemById(itemDto.getId());
        if (!itemToUpdate.getOwner().getId().equals(ownerId))
            throw new ValidationException("Пользователь не является владельцем предмета");
        if (Objects.nonNull(itemDto.getName()) && !itemToUpdate.getName().equals(itemDto.getName()))
            itemToUpdate.setName(itemDto.getName());
        if (Objects.nonNull(itemDto.getDescription()) && !itemToUpdate.getDescription().equals(itemDto.getDescription()))
            itemToUpdate.setDescription(itemDto.getDescription());
        if (Objects.nonNull(itemDto.getIsAvailableStatus()) && !itemToUpdate.getIsAvailableStatus().equals(itemDto.getIsAvailableStatus()))
            itemToUpdate.setIsAvailableStatus(itemDto.getIsAvailableStatus());
        return ItemMapper.jpaToDto(itemToUpdate);
    }

    @Override
    public ItemDto deleteItemById(Long id) {
        getItemById(id);
        return ItemMapper.jpaToDto(itemStorage.deleteItem(id));
    }
}
