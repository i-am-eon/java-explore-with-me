package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.enums.RequestStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    @NotEmpty(message = "Список заявок не может быть пустым")
    private List<Long> requestIds;

    @NotNull(message = "Статус заявки обязателен")
    private RequestStatus status;
}