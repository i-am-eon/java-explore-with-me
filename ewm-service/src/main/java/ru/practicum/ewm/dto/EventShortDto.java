package ru.practicum.ewm.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private Long id;
    private String annotation;
    private LocalDateTime eventDate;
    private Boolean paid;
    private String title;
    private CategoryDto category;
    private UserShortDto initiator;
    private Long confirmedRequests;
    private Long views;
}