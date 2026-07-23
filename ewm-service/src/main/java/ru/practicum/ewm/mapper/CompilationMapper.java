package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {

        if (compilation == null) {
            return null;
        }

        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setEvents(compilation.getEvents() == null ? Set.of() : compilation.getEvents()
                .stream()
                .map(event -> EventMapper.toEventShortDto(event, 0L, 0L))
                .collect(Collectors.toSet()));

        return compilationDto;
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {

        if (newCompilationDto == null || events == null) {
            return null;
        }

        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setEvents(events);

        return compilation;
    }
}