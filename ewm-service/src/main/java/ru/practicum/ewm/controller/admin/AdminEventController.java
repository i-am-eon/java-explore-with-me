package ru.practicum.ewm.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.service.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;


    @GetMapping
    public List<EventFullDto> search(

            @RequestParam(required = false)
            List<Long> users,

            @RequestParam(required = false)
            List<EventState> states,

            @RequestParam(required = false)
            List<Long> categories,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime rangeStart,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime rangeEnd,

            @RequestParam(defaultValue = "0")
            int from,

            @RequestParam(defaultValue = "10")
            int size
    ) {
        return adminEventService.search(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId, @Valid @RequestBody UpdateEventAdminRequest request) {
        return adminEventService.update(eventId, request);
    }
}