package ru.practicum.shareit.item.service;

import jakarta.validation.constraints.Positive;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;

import java.util.Collection;

public interface ItemService {
    @Transactional(readOnly = true)
    Collection<ItemDtoFull> getUsersItemsDto(Long ownerId);

    @Transactional(readOnly = true)
    ItemDtoFull getItemDtoById(Long id);

    @Transactional(readOnly = true)
    Collection<ItemDto> getAvailableItemsDtoByText(@Positive String searchText);

    @Transactional
    ItemDto addItem(Long ownerId, ItemDto itemDto);

    @Transactional
    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);

    @Transactional
    void deleteItemById(Long ownerId, Long itemId);

    @Transactional
    CommentDto addComment(Long userId, @Positive Long itemId, Comment comment);
}
