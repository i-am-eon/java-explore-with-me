package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    @Query("""
            SELECT e
            FROM Event e
            WHERE (:users IS NULL OR e.initiator.id IN :users)
            AND (:states IS NULL OR e.state IN :states)
            AND (:categories IS NULL OR e.category.id IN :categories)
            AND (e.eventDate >= :rangeStart)
            AND (e.eventDate <= :rangeEnd)
    """)
    Page<Event> search(
            @Param("users") List<Long> users,
            @Param("states") List<EventState> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.state = ru.practicum.ewm.model.enums.EventState.PUBLISHED
            AND (:text IS NULL
                OR (LOWER(e.annotation) LIKE '%' || LOWER(:text) || '%')
                OR (LOWER(e.description) LIKE '%' || LOWER(:text) || '%'))
            AND (:categories IS NULL OR e.category.id IN :categories)
            AND (:paid IS NULL OR e.paid = :paid)
            AND (e.eventDate >= :rangeStart)
            AND (e.eventDate <= :rangeEnd)
            AND e.eventDate >= CURRENT_TIMESTAMP
            AND (:onlyAvailable = false
                OR e.participantLimit = 0
                OR (
                        SELECT COUNT(pr)
                        FROM ParticipationRequest pr
                        WHERE pr.event.id = e.id
                        AND pr.status = ru.practicum.ewm.model.enums.RequestStatus.CONFIRMED
                    ) < e.participantLimit
                )
    """)
    Page<Event> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                          LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);
}