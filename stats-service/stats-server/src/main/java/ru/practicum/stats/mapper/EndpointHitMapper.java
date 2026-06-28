package ru.practicum.stats.mapper;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.model.EndpointHit;

public class EndpointHitMapper {

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {

        if (endpointHit == null) {
            return null;
        }

        EndpointHitDto endpointHitDto = new EndpointHitDto();

        endpointHitDto.setApp(endpointHit.getApp());
        endpointHitDto.setUri(endpointHit.getUri());
        endpointHitDto.setIp(endpointHit.getIp());
        endpointHitDto.setTimestamp(endpointHit.getTimestamp());

        return endpointHitDto;
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {

        if (endpointHitDto == null) {
            return null;
        }

        EndpointHit endpointHit = new EndpointHit();

        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setTimestamp(endpointHitDto.getTimestamp());

        return endpointHit;
    }
}