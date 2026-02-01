package ru.practicum.shareit.item.comments.mapper;

import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.model.Comment;

public class CommentMapper {

    public static CommentDto jpaToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                comment.getCreated());
    }

    public static Comment dtoToJpa(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItem(),
                commentDto.getAuthor(),
                commentDto.getCreated());
    }
}
