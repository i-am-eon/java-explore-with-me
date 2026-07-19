package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto);

    void delete(Long categoryId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getById(Long categoryId);
}