package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto create(Long userId, Long eventId, NewCommentDto dto);

    CommentDto update(Long userId, Long commentId, NewCommentDto dto);

    void delete(Long userId, Long commentId);

    CommentDto getById(Long commentId);

    List<CommentDto> getUserComments(Long userId);

    List<CommentDto> getEventComments(Long eventId);
}