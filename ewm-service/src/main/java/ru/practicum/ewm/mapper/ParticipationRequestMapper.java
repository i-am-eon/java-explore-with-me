package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.User;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {

        if (participationRequest == null) {
            return null;
        }

        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(participationRequest.getId());

        participationRequestDto.setEventId(participationRequest.getEvent() != null
                ? participationRequest.getEvent().getId() : null);

        participationRequestDto.setRequesterId(participationRequest.getRequester() != null
                ? participationRequest.getRequester().getId() : null);

        participationRequestDto.setStatus(participationRequest.getStatus());
        participationRequestDto.setCreated(participationRequest.getCreated());

        return participationRequestDto;
    }

    public static ParticipationRequest toParticipationRequest(ParticipationRequestDto participationRequestDto,
                                                              Event event, User requester) {

        if (participationRequestDto == null || event == null || requester == null) {
            return null;
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setId(participationRequestDto.getId());
        participationRequest.setEvent(event);
        participationRequest.setRequester(requester);
        participationRequest.setStatus(participationRequestDto.getStatus());
        participationRequest.setCreated(participationRequestDto.getCreated());

        return participationRequest;
    }
}