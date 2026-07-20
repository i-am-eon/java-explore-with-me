package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Constants;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.RequestStatus;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.service.AdminEventService;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventFullDto> search(List<Long> users, List<EventState> states, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Некорректные параметры даты.");
        }

        if (from < 0 || size <= 0) {
            throw new ValidationException("Некорректные параметры пагинации.");
        }

        users = (users == null || users.isEmpty()) ? null : users;
        states = (states == null || states.isEmpty()) ? null : states;
        categories = (categories == null || categories.isEmpty()) ? null : categories;

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Event> events = eventRepository.search(users, states, categories, rangeStart, rangeEnd, pageable);

        if (events.isEmpty()) {
            return List.of();
        }

        List<Long> eventIds = events.getContent().stream()
                .map(Event::getId)
                .toList();

        Map<Long, Long> confirmedMap = getConfirmedRequestsMap(eventIds);

        List<String> uris = events.getContent().stream()
                .map(e -> "/events/" + e.getId())
                .toList();

        Map<String, Long> viewsMap = getViewsMap(uris);

        return events.getContent().stream()
                .map(event -> EventMapper.toEventFullDto(
                        event,
                        confirmedMap.getOrDefault(event.getId(), 0L),
                        viewsMap.getOrDefault("/events/" + event.getId(), 0L)
                ))
                .toList();
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено."));

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {

            if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ValidationException("Дата события должна быть не ранее чем через час.");
            }

            event.setEventDate(request.getEventDate());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getCategory() != null) {

            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория с id=" + request.getCategory() + " не найдена."));

            event.setCategory(category);
        }

        if (request.getStateAction() != null) {

            switch (request.getStateAction()) {

                case PUBLISH_EVENT -> {

                    if (event.getState() != EventState.PENDING) {
                        throw new ConflictException("Публиковать событие можно только со статусом PENDING.");
                    }

                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }

                case REJECT_EVENT -> {

                    if (event.getState() != EventState.PENDING) {
                        throw new ConflictException("Отклонить можно только событие в состоянии PENDING.");
                    }

                    event.setState(EventState.CANCELED);
                }
            }
        }

        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }

        Event savedEvent = eventRepository.save(event);

        long views = getViews(savedEvent.getId());

        long confirmed = participationRequestRepository.countByEventIdAndStatus(
                savedEvent.getId(),
                RequestStatus.CONFIRMED
        );

        return EventMapper.toEventFullDto(savedEvent, confirmed, views);
    }

    private Map<Long, Long> getConfirmedRequestsMap(List<Long> eventIds) {
        return participationRequestRepository.countConfirmedByEventIds(eventIds)
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }

    private Map<String, Long> getViewsMap(List<String> uris) {
        return statsClient.getViewStats(
                        Constants.STATS_START,
                        LocalDateTime.now(),
                        uris,
                        true
                )
                .stream()
                .collect(Collectors.toMap(
                        ViewStatsDto::getUri,
                        ViewStatsDto::getHits
                ));
    }

    private long getViews(Long eventId) {
        return getViewsMap(List.of("/events/" + eventId))
                .getOrDefault("/events/" + eventId, 0L);
    }
}