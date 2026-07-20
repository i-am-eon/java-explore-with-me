package ru.practicum.ewm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.dto.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(NotFoundException e) {

        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        apiError.setReason("Объект не существует");
        apiError.setStatus(HttpStatus.NOT_FOUND.name());
        apiError.setTimestamp(LocalDateTime.now());

        return apiError;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(ValidationException e) {

        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        apiError.setReason("Ошибка валидации");
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setTimestamp(LocalDateTime.now());

        return apiError;
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(ConflictException e) {

        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        apiError.setReason("Конфликт данных");
        apiError.setStatus(HttpStatus.CONFLICT.name());
        apiError.setTimestamp(LocalDateTime.now());

        return apiError;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ApiError apiError = new ApiError();
        apiError.setMessage("Ошибка валидации");
        apiError.setReason("Некорректные параметры запроса");
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .toList()
        );
        return apiError;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotReadable(HttpMessageNotReadableException e) {
        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMostSpecificCause().getMessage());
        apiError.setReason("Ошибка чтения JSON");
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }
}