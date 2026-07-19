package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.enums.RequestStatus;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    long countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    @Query("""
    SELECT r.event.id, COUNT(r)
    FROM ParticipationRequest r
    WHERE r.event.id IN :eventIds
      AND r.status = 'CONFIRMED'
    GROUP BY r.event.id
    """)
    List<Object[]> countConfirmedByEventIds(List<Long> eventIds);
}