package com.acharya.dikshanta.EcomMed.specifications;

import com.acharya.dikshanta.EcomMed.dto.request.ProductSearchRequest;
import com.acharya.dikshanta.EcomMed.model.Category;
import com.acharya.dikshanta.EcomMed.model.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ProductSpecification {
    public static Specification<Product> getSpecifications(ProductSearchRequest request) {
        return new Specification<Product>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Product> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
                List<Predicate> predicates = new ArrayList<>();
                if (request.name() != null && !request.name().isBlank()) {
                    Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + request.name().toLowerCase() + "%");
                    Predicate categoryPredicate = cb.like(cb.lower(categoryJoin.get("name")), "%" + request.name().toLowerCase() + "%");
                    predicates.add(cb.or(namePredicate, categoryPredicate));
                }

                if (request.minPrice() != null && request.maxPrice() != null) {
                    predicates.add(cb.between(
                            root.get("price"),
                            request.minPrice(),
                            request.maxPrice()));
                }

                if (request.maxPrice() == null && request.minPrice() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(
                            root.get("price"),
                            request.minPrice()));
                }
                if (request.minPrice() == null && request.maxPrice() != null) {

                    predicates.add(cb.lessThanOrEqualTo(
                            root.get("price"),
                            request.maxPrice()));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
