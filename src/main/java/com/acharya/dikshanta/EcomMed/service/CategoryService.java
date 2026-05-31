package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.dto.request.CategoryRequest;
import com.acharya.dikshanta.EcomMed.dto.response.CategoryResponse;
import com.acharya.dikshanta.EcomMed.dto.response.PagedResponse;
import com.acharya.dikshanta.EcomMed.exceptions.BusinessException;
import com.acharya.dikshanta.EcomMed.mappers.CategoryMapper;
import com.acharya.dikshanta.EcomMed.model.Category;
import com.acharya.dikshanta.EcomMed.repository.CategoryRepository;
import com.acharya.dikshanta.EcomMed.utils.constrants.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new BusinessException(MessageConstants.CategoryConstants.CATEGORY_ALREADY_EXISTS);
        }
        Category category = Category.builder()
                .name(request.name())
                .description(request.description())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(savedCategory.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public PagedResponse<CategoryResponse> findAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        Page<CategoryResponse> categoryResponsePage = categoryPage.map(categoryMapper::toResponse);
        return PagedResponse.toPagedResponse(categoryResponsePage);
    }
}
