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

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthor(UserMapper.toUserShortDto(comment.getAuthor()));
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated());
        commentDto.setUpdated(comment.getUpdated());

        return commentDto;
    }

    public static Comment toComment(NewCommentDto newCommentDto, User user, Event event) {

        if (newCommentDto == null || user == null || event == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setText(newCommentDto.getText());

        return comment;
    }
}