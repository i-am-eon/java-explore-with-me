package ru.practicum.ewm.controller.publicapi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.service.PublicCommentService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicCommentController {

    private final PublicCommentService publicCommentService;

    @GetMapping("/comments/{commentId}")
    public CommentDto getById(@PathVariable Long commentId) {
        return publicCommentService.getById(commentId);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getEventComments(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return publicCommentService.getEventComments(eventId, from, size);
    }
}