package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDtoOutput {
    private Long id;
    private String description;
    private Long requester;
    private LocalDateTime created;
    private List<ItemDto> items;

    public ItemRequestDtoOutput(Long id, String description, Long requester, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requester = requester;
        this.created = created;
    }
}
