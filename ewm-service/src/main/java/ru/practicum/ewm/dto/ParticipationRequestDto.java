package ru.practicum.ewm.dto;

import ru.practicum.ewm.model.enums.RequestStatus;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    private Long id;
    private Long eventId;
    private Long requesterId;
    private RequestStatus status;
    private LocalDateTime created;
}