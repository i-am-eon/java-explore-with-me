package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("""
        SELECT new ru.practicum.stats.dto.ViewStatsDto(
            h.app,
            h.uri,
            COUNT(h.id)
        )
        FROM EndpointHit h
        WHERE h.timestamp BETWEEN :start AND :end
          AND (:uris IS NULL OR h.uri IN :uris)
        GROUP BY h.app, h.uri
        ORDER BY COUNT(h.id) DESC
        """)
    List<ViewStatsDto> getStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("""
        SELECT new ru.practicum.stats.dto.ViewStatsDto(
            h.app,
            h.uri,
            COUNT(DISTINCT h.ip)
        )
        FROM EndpointHit h
        WHERE h.timestamp BETWEEN :start AND :end
          AND (:uris IS NULL OR h.uri IN :uris)
        GROUP BY h.app, h.uri
        ORDER BY COUNT(h.ip) DESC
        """)
    List<ViewStatsDto> getUniqueStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );
}