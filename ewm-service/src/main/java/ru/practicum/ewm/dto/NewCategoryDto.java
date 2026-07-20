package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {

    @NotBlank (message = "Название категории обязательно")
    @Size(max = 50, message = "Название категории не должно превышать 50 символов")
    private String name;
}