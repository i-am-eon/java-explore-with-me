package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventUserRequest;

import java.util.List;

public interface PrivateEventService {

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto create(Long userId, NewEventDto newEventDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest request);
}