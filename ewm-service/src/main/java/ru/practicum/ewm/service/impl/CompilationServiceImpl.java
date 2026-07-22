package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationRequest;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.CompilationService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {

        if (compilationRepository.existsByTitle(newCompilationDto.getTitle())) {
            throw new ConflictException("Подборка с title=" + newCompilationDto.getTitle() + " уже существует.");
        }

        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());

        compilation.setPinned(
                newCompilationDto.getPinned() != null
                        ? newCompilationDto.getPinned()
                        : false
        );

        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(newCompilationDto.getEvents()
                    .stream()
                    .map(eventId -> eventRepository.findById(eventId)
                            .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено.")))
                    .collect(Collectors.toSet()));
        } else {
            compilation.setEvents(Collections.emptySet());
        }

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto update(Long compilationId, UpdateCompilationRequest request) {

        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(
                        "Подборка с id=" + compilationId + " не найдена."
                ));

        if (request.getTitle() != null) {
            if (!compilation.getTitle().equals(request.getTitle())
                    && compilationRepository.existsByTitle(request.getTitle())) {
                throw new ConflictException("Подборка с title=" + request.getTitle() + " уже существует.");
            }

            compilation.setTitle(request.getTitle());
        }

        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }

        if (request.getEvents() != null) {
            compilation.setEvents(request.getEvents()
                    .stream()
                    .map(eventId -> eventRepository.findById(eventId)
                            .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено.")))
                    .collect(Collectors.toSet())
            );
        }

        return CompilationMapper.toCompilationDto(
                compilationRepository.save(compilation)
        );
    }

    @Override
    public void delete(Long compilationId) {

        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Подборка с id=" + compilationId + " не найдена."));

        compilationRepository.delete(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {

        if (from < 0 || size <= 0) {
            throw new ValidationException("Некорректные параметры пагинации.");
        }

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Compilation> compilations;

        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable);
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        }

        return compilations.getContent().stream().map(CompilationMapper::toCompilationDto).toList();
    }

    @Override
    public CompilationDto getById(Long compilationId) {

        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Подборка с id=" + compilationId + " не найдена."));

        return CompilationMapper.toCompilationDto(compilation);
    }
}