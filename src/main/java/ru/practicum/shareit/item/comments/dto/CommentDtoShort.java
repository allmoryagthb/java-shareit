package ru.practicum.shareit.item.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoShort {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
