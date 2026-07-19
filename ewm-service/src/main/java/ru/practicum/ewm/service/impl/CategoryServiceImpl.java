package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {

        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Категория с name=" + newCategoryDto.getName() + " уже существует.");
        }

        Category category = CategoryMapper.toCategory(newCategoryDto);

        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + categoryId + " не найдена."));

        if (!category.getName().equals(newCategoryDto.getName())
                && categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Категория с name=" + newCategoryDto.getName() + " уже существует.");
        }

        category.setName(newCategoryDto.getName());

        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long categoryId) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Категория с id=" + categoryId + " не найдена.");
        }

        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {

        if (from < 0 || size <= 0) {
            throw new ValidationException("Некорректные параметры пагинации.");
        }

        Pageable pageable = PageRequest.of(from / size, size);

        return categoryRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long categoryId) {

        return CategoryMapper.toCategoryDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + categoryId + " не найдена.")));
    }
}