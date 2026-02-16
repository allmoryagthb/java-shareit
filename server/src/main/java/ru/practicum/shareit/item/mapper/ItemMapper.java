package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;

import java.util.Objects;

public class ItemMapper {

    public static Item dtoToJpa(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                null);
    }

    public static ItemDto jpaToDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
        if (Objects.nonNull(item.getRequest()))
            itemDto.setRequestId(item.getRequest().getId());
        return itemDto;
    }

    public static ItemDtoFull jpaToDtoFull(Item item) {
        ItemDtoFull itemDtoFull = new ItemDtoFull(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null);

        if (Objects.nonNull(item.getRequest()))
            itemDtoFull.setItemRequestDtoOutput(ItemRequestMapper.jpaToDtoOutput(item.getRequest()));

        return itemDtoFull;
    }

    public static ItemDtoFull dtoToDtoFull(ItemDto itemDto) {
        return new ItemDtoFull(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
    }
}
