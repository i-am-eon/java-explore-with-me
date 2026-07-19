package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    @NotBlank(message = "Название подборки обязательно")
    private String title;

    private Boolean pinned;

    @NotEmpty(message = "Список событий не должен быть пустым")
    private Set<Long> events;
}