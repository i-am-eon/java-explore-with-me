package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(NewUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Пользователь с email=" + request.getEmail() + " уже существует.");
        }

        User user = UserMapper.toUser(request);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void delete(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден.");
        }

        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {

        if (from < 0 || size <= 0) {
            throw new ValidationException("Некорректные параметры пагинации.");
        }

        Pageable pageable = PageRequest.of(from / size, size);

        List<User> users;

        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.findAllByIdIn(ids, pageable).getContent();
        }

        return users.stream().map(UserMapper::toUserDto).toList();
    }
}