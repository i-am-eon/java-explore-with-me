package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.RequestStatus;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.ParticipationRequestService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {

        if (participationRequestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Заявка уже существует.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден."));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено."));

        long confirmed = participationRequestRepository.countByEventIdAndStatus(
                eventId, RequestStatus.CONFIRMED);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя отправить заявку на участие в собственном событии.");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя отправить заявку на неопубликованное событие.");
        }

        if (event.getParticipantLimit() != 0
                && confirmed >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит участников события.");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setRequester(user);

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        request.setCreated(LocalDateTime.now());

        ParticipationRequest savedRequest = participationRequestRepository.save(request);

        return ParticipationRequestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден.");
        }

        ParticipationRequest request = participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявка с id=" + requestId + " не найден."));

        if (!request.getRequester().getId().equals(userId)) {
            throw new NotFoundException("Заявка с id=" + requestId + " не найдена.");
        }

        request.setStatus(RequestStatus.CANCELED);

        ParticipationRequest savedRequest = participationRequestRepository.save(request);

        return ParticipationRequestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден.");
        }

        List<ParticipationRequest> requests = participationRequestRepository.findAllByRequesterId(userId);

        return requests.stream().map(ParticipationRequestMapper::toParticipationRequestDto).toList();
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {

        eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено."));

        List<ParticipationRequest> requests = participationRequestRepository.findAllByEventId(eventId);

        return requests.stream().map(ParticipationRequestMapper::toParticipationRequestDto).toList();

    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено."));

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Подтверждение заявок для данного события не требуется.");
        }

        List<ParticipationRequest> requests = participationRequestRepository.findAllById(request.getRequestIds());

        for (ParticipationRequest r : requests) {

            if (!r.getEvent().getId().equals(eventId)) {
                throw new ConflictException("Заявка не относится к данному событию.");
            }

            if (r.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Можно изменить только заявки в статусе PENDING.");
            }
        }

        if (request.getStatus() == RequestStatus.CONFIRMED) {

            long confirmed = participationRequestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

            int limit = event.getParticipantLimit();

            if (limit != 0 && confirmed >= limit) {
                throw new ConflictException("Достигнут лимит участников события.");
            }

            for (ParticipationRequest r : requests) {

                if (limit != 0 && confirmed >= limit) {
                    r.setStatus(RequestStatus.REJECTED);
                } else {
                    r.setStatus(RequestStatus.CONFIRMED);
                    confirmed++;
                }
            }

        } else {

            for (ParticipationRequest r : requests) {
                r.setStatus(RequestStatus.REJECTED);
            }
        }

        participationRequestRepository.saveAll(requests);

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        result.setConfirmedRequests(
                requests.stream()
                        .filter(r -> r.getStatus() == RequestStatus.CONFIRMED)
                        .map(ParticipationRequestMapper::toParticipationRequestDto)
                        .toList());

        result.setRejectedRequests(
                requests.stream()
                        .filter(r -> r.getStatus() == RequestStatus.REJECTED)
                        .map(ParticipationRequestMapper::toParticipationRequestDto)
                        .toList());

        return result;
    }
}