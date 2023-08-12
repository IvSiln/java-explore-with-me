package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

import static ru.practicum.utils.ExploreConstantsAndStaticMethods.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper catMapper;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto category) {
        Category newCategory = catMapper.toCategory(category);
        checkNewCatNameIsUnique(category.getName(), null);
        Category saved = categoryRepository.save(newCategory);
        return catMapper.toCategoryDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        Category category = getCategoryIfExists(catId);
        checkNoEventWithCatExists(catId);
        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto, Long catId) {
        Category category = getCategoryIfExists(catId);
        checkNewCatNameIsUnique(categoryDto.getName(), category.getName());
        updateCategoryByDto(category, categoryDto);
        return catMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> get(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Category> catPage = categoryRepository.findAll(pageable);
        return catPage.map(catMapper::toCategoryDto).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto get(Long catId) {
        Category category = getCategoryIfExists(catId);
        return catMapper.toCategoryDto(category);
    }

    private void checkNewCatNameIsUnique(String newName, String name) {
        if (!newName.equals(name)) {
            categoryRepository.findFirst1ByName(newName).ifPresent(cat -> {
                throw new ConflictException(CATEGORY_NAME_ALREADY_EXISTS_EXCEPTION);
            });
        }
    }

    private void checkNoEventWithCatExists(Long catId) {
        if (eventRepository.findByCategoryId(catId).isPresent()) {
            throw new ConflictException(CATEGORY_IS_CONNECTED_WITH_EVENTS);
        }
    }

    private Category getCategoryIfExists(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND_EXCEPTION));
    }

    private void updateCategoryByDto(Category category, CategoryDto categoryDto) {
        String newName = categoryDto.getName();
        String existingName = category.getName();
        category.setName(StringUtils.defaultIfBlank(newName, existingName));
    }
}

