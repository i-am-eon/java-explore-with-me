package ru.practicum.ewm.controller.open;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.model.enums.EventSort;
import ru.practicum.ewm.service.PublicEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final PublicEventService publicEventService;

    @GetMapping
    public List<EventShortDto> getEvents(

            @RequestParam(required = false)
            String text,

            @RequestParam(required = false)
            List<Long> categories,

            @RequestParam(required = false)
            Boolean paid,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeStart,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime rangeEnd,

            @RequestParam(required = false)
            Boolean onlyAvailable,

            @RequestParam(required = false)
            EventSort sort,

            @RequestParam(defaultValue = "0")
            int from,

            @RequestParam(defaultValue = "10")
            int size,

            HttpServletRequest request
    ) {
        return publicEventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable("id") Long eventId, HttpServletRequest request) {
        return publicEventService.getEventById(eventId, request);
    }
}