package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CommentDto;

import java.util.List;

public interface PublicCommentService {

    CommentDto getById(Long commentId);

    List<CommentDto> getEventComments(Long eventId, int from, int size);
}