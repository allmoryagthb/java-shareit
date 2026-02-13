package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutput;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoFull {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private ItemRequestDtoOutput itemRequestDtoOutput;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;

    public ItemDtoFull(Long id, String name, String description, Boolean available, ItemRequestDtoOutput itemRequestDtoOutput) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.itemRequestDtoOutput = itemRequestDtoOutput;
    }
}
