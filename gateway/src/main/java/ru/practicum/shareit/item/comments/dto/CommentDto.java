package ru.practicum.shareit.item.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.item.model.Item;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private Item item;
    private ru.practicum.shareit.user.model.User author;
    private LocalDateTime created;
}
