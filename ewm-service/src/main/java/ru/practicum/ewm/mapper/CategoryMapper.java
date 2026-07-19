package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.model.Category;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {

        if (category == null) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        return categoryDto;
    }

    public static Category toCategory(CategoryDto categoryDto) {

        if (categoryDto == null) {
            return null;
        }

        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());

        return category;
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {

        if (newCategoryDto == null) {
            return null;
        }

        Category category = new Category();
        category.setName(newCategoryDto.getName());

        return category;
    }
}