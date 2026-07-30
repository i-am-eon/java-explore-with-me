package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {

        if (comment == null) {
            return null;
        }

        return CommentDto.builder()
                .id(comment.getId())
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .text(comment.getText())
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .build();
    }

    public static Comment toComment(NewCommentDto newCommentDto, User user, Event event) {

        if (newCommentDto == null || user == null || event == null) {
            return null;
        }

        return Comment.builder()
                .author(user)
                .event(event)
                .text(newCommentDto.getText())
                .build();
    }
}