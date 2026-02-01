package ru.practicum.shareit.item.service;

import jakarta.validation.constraints.Positive;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.CommentDtoShort;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDtoFull> getUserItems(Long ownerId);

    ItemDtoFull getItemDtoById(Long id);

    Collection<ItemDto> getAvailableItemsDtoByText(@Positive String searchText);

    ItemDto addItem(Long ownerId, ItemDto itemDto);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);

    void deleteItemById(Long ownerId, Long itemId);

    CommentDtoShort addComment(Long userId, @Positive Long itemId, CommentDto commentDto);
}
