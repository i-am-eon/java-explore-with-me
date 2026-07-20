package ru.practicum.ewm.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Constants;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.EventSort;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.RequestStatus;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.service.PublicEventService;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSort sort,
            int from,
            int size,
            HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Некорректные параметры даты.");
        }

        if (from < 0 || size <= 0) {
            throw new ValidationException("Некорректные параметры пагинации.");
        }

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(100);
        }

        Pageable pageable;

        if (sort == EventSort.EVENT_DATE) {
            pageable = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());
        } else {
            pageable = PageRequest.of(from / size, size);
        }

        if (text == null) {
            text = "";
        }

        Page<Event> events = eventRepository.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable);

        statsClient.saveEndpointHit(
                new EndpointHitDto(
                        "ewm-main-service",
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        LocalDateTime.now()
                )
        );

        if (events.isEmpty()) {
            return List.of();
        }

        List<Long> eventIds = events.getContent().stream()
                .map(Event::getId)
                .toList();

        Map<Long, Long> confirmedMap = getConfirmedRequestsMap(eventIds);

        List<String> uris = events.getContent().stream().map(e -> "/events/" + e.getId()).toList();

        Map<String, Long> viewsMap = getViewsMap(uris);

        List<EventShortDto> result = events.getContent().stream()
                .map(event -> EventMapper.toEventShortDto(
                        event,
                        confirmedMap.getOrDefault(event.getId(), 0L),
                        viewsMap.getOrDefault("/events/" + event.getId(), 0L)
                ))
                .toList();

        if (sort == EventSort.VIEWS) {
            return result.stream()
                    .sorted((e1, e2) -> Long.compare(e2.getViews(), e1.getViews()))
                    .toList();
        }

        return result;
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено."));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие с id=" + eventId + " не найдено.");
        }

        statsClient.saveEndpointHit(
                new EndpointHitDto(
                        "ewm-main-service",
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        LocalDateTime.now()
                )
        );

        long confirmed = participationRequestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        List<ViewStatsDto> stats = statsClient.getViewStats(
                Constants.STATS_START,
                LocalDateTime.now(),
                List.of(request.getRequestURI()),
                true);

        long views = stats.isEmpty() ? 0 : stats.getFirst().getHits();

        return EventMapper.toEventFullDto(event, confirmed, views);
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
}