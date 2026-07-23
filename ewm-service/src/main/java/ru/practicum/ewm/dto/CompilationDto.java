package ru.practicum.ewm.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private Long id;
    private String title;
    private Boolean pinned;
    private Set<EventShortDto> events;
}