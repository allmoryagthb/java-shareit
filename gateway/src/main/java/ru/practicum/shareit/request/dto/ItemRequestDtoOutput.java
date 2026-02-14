package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.server.item.dto.ItemDtoFull;

import java.time.LocalDate;
import java.util.List;

@Data
public class ItemRequestDtoOutput {
    private Long id;
    private String description;
    private Long requester;
    private LocalDate created;
    private List<ItemDtoFull> items;

    public ItemRequestDtoOutput(Long id, String description, Long requester, LocalDate created) {
        this.id = id;
        this.description = description;
        this.requester = requester;
        this.created = created;
    }
}
