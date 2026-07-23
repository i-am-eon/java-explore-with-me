package ru.practicum.ewm.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {

    @Size(max = 50, message = "Название подборки не должно превышать 50 символов")
    private String title;

    private Boolean pinned;
    private Set<Long> events;
}