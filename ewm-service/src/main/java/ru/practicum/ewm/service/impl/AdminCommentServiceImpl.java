package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.service.AdminCommentService;

@Service
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentRepository commentRepository;

    @Override
    public void delete(Long commentId) {

        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Комментарий с id=" + commentId + " не найден.");
        }

        commentRepository.deleteById(commentId);
    }
}