package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;

import java.util.List;

public interface PrivateCommentService {

    CommentDto create(Long userId, Long eventId, NewCommentDto dto);

    CommentDto update(Long userId, Long commentId, NewCommentDto dto);

    void delete(Long userId, Long commentId);

    List<CommentDto> getUserComments(Long userId, int from, int size);
}