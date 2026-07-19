package ru.practicum.ewm.controller.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class ParticipationRequestController {

    private final ParticipationRequestService participationRequestService;

    @PostMapping
    public ParticipationRequestDto create(@PathVariable("userId") Long userId, @RequestParam("eventId") Long eventId) {
        return participationRequestService.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable("userId") Long userId, @PathVariable("requestId") Long requestId) {
        return participationRequestService.cancel(userId, requestId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return participationRequestService.getUserRequests(userId);
    }
}