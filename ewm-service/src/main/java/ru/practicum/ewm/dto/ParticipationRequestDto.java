package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long event;
    private Long requester;
    private RequestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}