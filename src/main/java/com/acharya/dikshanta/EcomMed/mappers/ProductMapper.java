package com.acharya.dikshanta.EcomMed.mappers;

import com.acharya.dikshanta.EcomMed.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ProductCreateResponse;
import com.acharya.dikshanta.EcomMed.dto.response.ProductResponse;
import com.acharya.dikshanta.EcomMed.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductCreateRequest request);

    @Mapping(source = "category.name", target = "categoryName")
    ProductCreateResponse toResponse(Product product);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "inventory.quantity", target = "availableStock")
    ProductResponse toProductResponse(Product product);
}
