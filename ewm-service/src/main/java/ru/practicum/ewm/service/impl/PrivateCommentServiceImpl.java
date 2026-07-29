package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.PrivateCommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto newCommentDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден."));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено."));

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Комментировать можно только опубликованные события.");
        }

        Comment comment = CommentMapper.toComment(newCommentDto, user, event);
        comment.setCreated(LocalDateTime.now());
        comment.setUpdated(null);

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toCommentDto(savedComment);
    }

    @Override
    public CommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=" + commentId + " не найден."));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Редактировать можно только свои комментарии.");
        }

        comment.setText(newCommentDto.getText());
        comment.setUpdated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toCommentDto(savedComment);
    }

    @Override
    public void delete(Long userId, Long commentId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден.");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=" + commentId + " не найден."));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Нельзя удалить чужой комментарий.");
        }

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDto> getUserComments(Long userId, int from, int size) {

        if (from < 0 || size <= 0) {
            throw new ValidationException("Некорректные параметры пагинации.");
        }

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден.");
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());

        return commentRepository.findByAuthorId(userId, pageable).stream().map(CommentMapper::toCommentDto).toList();
    }
}