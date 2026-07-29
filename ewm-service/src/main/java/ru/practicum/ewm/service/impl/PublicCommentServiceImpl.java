package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto dto) {
        return null;
    }

    @Override
    public CommentDto update(Long userId, Long commentId, NewCommentDto dto) {
        return null;
    }

    @Override
    public void delete(Long userId, Long commentId) {

        if (userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден.");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=" + commentId + " не найден."));

        if(!comment.getAuthor().getId().equals(commentId)) {
            throw new ConflictException("Нельзя удалить чужой комментарий");
        }

        commentRepository.delete(comment);
    }

    @Override
    public CommentDto getById(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=" + commentId + " не найден."));

        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getUserComments(Long userId) {
        return List.of();
    }

    @Override
    public List<CommentDto> getEventComments(Long eventId) {
        return List.of();
    }
}
