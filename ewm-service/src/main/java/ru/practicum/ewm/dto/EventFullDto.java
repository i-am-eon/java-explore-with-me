package ru.practicum.ewm.dto;

import ru.practicum.ewm.model.enums.EventState;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {

    private Long id;
    private String annotation;
    private String description;
    private LocalDateTime createdOn;
    private LocalDateTime eventDate;
    private LocalDateTime publishedOn;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private CategoryDto category;
    private UserShortDto initiator;
    private LocationDto location;
    private Long confirmedRequests;
    private Long views;
}