package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.PublicCommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCommentServiceImpl implements PublicCommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto getById(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=" + commentId + " не найден."));

        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getEventComments(Long eventId, int from, int size) {

        if (from < 0 || size <= 0) {
            throw new ValidationException(String.format("Некорректные параметры пагинации: eventId=%d, from=%d, size=%d.", eventId, from, size));
        }

        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие с id=" + eventId + " не найден.");
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());

        return commentRepository.findByEventId(eventId, pageable).stream().map(CommentMapper::toCommentDto).toList();
    }
}