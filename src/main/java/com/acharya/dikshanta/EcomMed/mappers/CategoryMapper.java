package com.acharya.dikshanta.EcomMed.mappers;

import com.acharya.dikshanta.EcomMed.dto.response.CategoryResponse;
import com.acharya.dikshanta.EcomMed.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
}
