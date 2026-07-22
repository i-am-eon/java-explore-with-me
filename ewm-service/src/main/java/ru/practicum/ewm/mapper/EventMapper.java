package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;

public class EventMapper {

    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views) {

        if (event == null) {
            return null;
        }

        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .publishedOn(event.getPublishedOn())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toDto(event.getLocation()))
                .confirmedRequests(confirmedRequests != null ? confirmedRequests : 0L)
                .views(views != null ? views : 0L)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event, Long confirmedRequests, Long views) {

        if (event == null) {
            return null;
        }

        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .title(event.getTitle())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .confirmedRequests(confirmedRequests != null ? confirmedRequests : 0L)
                .views(views != null ? views : 0L)
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator) {

        if (newEventDto == null || category == null || initiator == null) {
            return null;
        }

        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())

                .paid(
                        newEventDto.getPaid() != null
                                ? newEventDto.getPaid()
                                : false
                )

                .participantLimit(
                        newEventDto.getParticipantLimit() != null
                                ? newEventDto.getParticipantLimit()
                                : 0
                )

                .requestModeration(
                        newEventDto.getRequestModeration() != null
                                ? newEventDto.getRequestModeration()
                                : true
                )

                .title(newEventDto.getTitle())
                .category(category)
                .initiator(initiator)
                .location(LocationMapper.toEntity(newEventDto.getLocation()))
                .build();
    }
}