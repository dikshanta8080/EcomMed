package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.EcomMed.dto.request.ProductSearchRequest;
import com.acharya.dikshanta.EcomMed.dto.request.UpdateStockRequest;
import com.acharya.dikshanta.EcomMed.dto.response.PagedResponse;
import com.acharya.dikshanta.EcomMed.dto.response.ProductCreateResponse;
import com.acharya.dikshanta.EcomMed.dto.response.ProductResponse;
import com.acharya.dikshanta.EcomMed.events.ProductCreatedEvent;
import com.acharya.dikshanta.EcomMed.exceptions.BusinessException;
import com.acharya.dikshanta.EcomMed.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.EcomMed.mappers.ProductMapper;
import com.acharya.dikshanta.EcomMed.model.Category;
import com.acharya.dikshanta.EcomMed.model.Product;
import com.acharya.dikshanta.EcomMed.repository.CategoryRepository;
import com.acharya.dikshanta.EcomMed.repository.InventoryRepository;
import com.acharya.dikshanta.EcomMed.repository.ProductRepository;
import com.acharya.dikshanta.EcomMed.specifications.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final InventoryRepository inventoryRepository;
    private final ImageUploadService imageUploadService;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request, MultipartFile multipartFile) {
        String file = imageUploadService.saveImage(multipartFile);
        if (productRepository.existsByName(request.name())) {
            throw new BusinessException(MessageConstants.ProductConstants.PRODUCT_EXISTS);
        }
        checkQuantity(request.quantity());
        Category category = categoryRepository.findById(request.CategoryId()).orElseThrow(() ->
                new ResourceNotFoundException(MessageConstants.CategoryConstants.CATEGORY_NOT_FOUND));
        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setImageUrl(file);
        Product savedProduct = productRepository.save(product);

        eventPublisher.publishEvent(new ProductCreatedEvent(savedProduct, request.quantity()));
        return productMapper.toResponse(savedProduct);

    }

    @org.springframework.transaction.annotation.Transactional
    public ProductCreateResponse updateStock(UpdateStockRequest request) {
        Product product = productRepository.findById(request.productId()).orElseThrow(() ->
                new ResourceNotFoundException(MessageConstants.ProductConstants.PRODUCT_NOT_FOUND));
        checkQuantity(request.quantity());
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    private void checkQuantity(Integer quantity) {
        if ((quantity < 1)) {
            throw new BusinessException(MessageConstants.ProductConstants.INVALID_QUANTITY);
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public PagedResponse<ProductResponse> findAllProducts(Pageable pageable, ProductSearchRequest request) {
        Specification<Product> specifications = ProductSpecification.getSpecifications(request);
        Page<Product> allProducts = productRepository.findAll(specifications, pageable);

        Page<ProductResponse> productResponses = allProducts.map(productMapper::toProductResponse);
        return PagedResponse.toPagedResponse(productResponses);

    }
}
