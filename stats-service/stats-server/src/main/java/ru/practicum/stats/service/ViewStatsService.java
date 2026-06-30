package ru.practicum.stats.service;

import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ViewStatsService {

    List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}