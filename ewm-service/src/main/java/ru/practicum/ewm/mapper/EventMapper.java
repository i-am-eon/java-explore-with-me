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

        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setConfirmedRequests(confirmedRequests != null ? confirmedRequests : 0);
        eventFullDto.setViews(views != null ? views : 0);

        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event, Long confirmedRequests, Long views) {

        if (event == null) {
            return null;
        }

        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setConfirmedRequests(confirmedRequests != null ? confirmedRequests : 0);
        eventShortDto.setViews(views != null ? views : 0);

        return eventShortDto;
    }

    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator) {

        if(newEventDto == null || category == null || initiator == null) {
            return null;
        }

        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setLocation(newEventDto.getLocation());

        return event;
    }
}