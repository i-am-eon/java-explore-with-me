package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(NewUserRequest request);

    void delete(Long userId);

    List<UserDto> getUsers(List<Long> ids, int from, int size);
}