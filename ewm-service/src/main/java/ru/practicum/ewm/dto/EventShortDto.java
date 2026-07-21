package ru.practicum.ewm.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private Long id;
    private String annotation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;
    private String title;
    private CategoryDto category;
    private UserShortDto initiator;
    private Long confirmedRequests;
    private Long views;
}