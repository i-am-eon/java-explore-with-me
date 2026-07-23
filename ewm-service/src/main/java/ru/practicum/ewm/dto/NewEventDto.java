package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank(message = "Аннотация обязательна")
    @Size(min = 20, max = 2000, message = "Аннотация должна быть от 20 до 2000 символов")
    private String annotation;

    @NotBlank(message = "Описание обязательно")
    @Size(min = 20, max = 7000, message = "Описание должно быть от 20 до 7000 символов")
    private String description;

    @NotNull(message = "Дата события обязательна")
    @Future(message = "Дата события должна быть в будущем")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;

    @PositiveOrZero(message = "Количество участников не может быть отрицательным")
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "Название события обязательно")
    @Size(min = 3, max = 120, message = "Название должно быть от 3 до 120 символов")
    private String title;

    @NotNull(message = "Категория обязательна")
    private Long category;

    @Valid
    @NotNull(message = "Локация обязательна")
    private LocationDto location;
}