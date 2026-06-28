package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.repository.EndpointHitRepository;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void saveEndpointHit(EndpointHitDto endpointHitDto) {

        if (endpointHitDto.getApp() == null || endpointHitDto.getUri() == null || endpointHitDto.getIp() == null
        || endpointHitDto.getTimestamp() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        endpointHitRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }
}