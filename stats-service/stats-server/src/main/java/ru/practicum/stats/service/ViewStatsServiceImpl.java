package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewStatsServiceImpl implements ViewStatsService {

    private final EndpointHitRepository endpointHitRepository;

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                           boolean unique) {

        if (start == null || end == null || start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (unique) {
            return endpointHitRepository.getUniqueStats(start, end, uris);
        }

        return endpointHitRepository.getStats(start, end, uris);
    }
}