package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.server.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.server.request.dto.ItemRequestDtoOutput;
import ru.practicum.shareit.server.request.model.ItemRequest;

public class ItemRequestMapper {

    public static ItemRequestDtoOutput jpaToDtoOutput(ItemRequest itemRequest) {
        return new ItemRequestDtoOutput(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequesterId(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequest dtoInputToJpa(ItemRequestDtoInput itemRequestDtoInput) {
        return new ItemRequest(
                itemRequestDtoInput.getId(),
                itemRequestDtoInput.getDescription()
        );
    }
}
