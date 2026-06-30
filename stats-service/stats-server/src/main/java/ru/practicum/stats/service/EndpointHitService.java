package ru.practicum.stats.service;

import ru.practicum.stats.dto.EndpointHitDto;

public interface EndpointHitService {

    void saveEndpointHit(EndpointHitDto endpointHitDto);
}