package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @NotNull(message = "Широта обязательна")
    private Float lat;

    @NotNull(message = "Долгота обязательна")
    private Float lon;
}