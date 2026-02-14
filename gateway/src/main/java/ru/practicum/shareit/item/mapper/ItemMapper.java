package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.model.Item;

public class ItemMapper {

    public static Item dtoToJpa(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                itemDto.getItemRequest());
    }

    public static ItemDto jpaToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest());
    }

    public static ItemDtoFull jpaToDtoFull(Item item) {
        ItemRequestDtoOutput itemRequestDtoOutput = ItemRequestMapper.jpaToDtoOutput(item.getRequest());

        return new ItemDtoFull(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                itemRequestDtoOutput);
    }
}
