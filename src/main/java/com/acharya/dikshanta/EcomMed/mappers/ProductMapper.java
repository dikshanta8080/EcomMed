package com.acharya.dikshanta.EcomMed.mappers;

import com.acharya.dikshanta.EcomMed.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ProductResponse;
import com.acharya.dikshanta.EcomMed.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductCreateRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    ProductResponse toResponse(Product product);
}
