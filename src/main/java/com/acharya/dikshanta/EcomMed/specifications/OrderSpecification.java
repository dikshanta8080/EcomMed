package com.acharya.dikshanta.EcomMed.specifications;

import com.acharya.dikshanta.EcomMed.dto.request.OrderFilterRequest;
import com.acharya.dikshanta.EcomMed.model.Order;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> getSpecification(OrderFilterRequest request) {

        return (Root<Order> root,
                CriteriaQuery<?> query,
                CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request.startDate() != null && request.endDate() != null) {

                LocalDateTime start = toStartOfDay(request.startDate());
                LocalDateTime end = toEndOfDay(request.endDate());

                predicates.add(cb.between(root.get("createdAt"), start, end));
            }

            if (request.startDate() != null && request.endDate() == null) {

                LocalDateTime start = toStartOfDay(request.startDate());

                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), start));
            }

            if (request.startDate() == null && request.endDate() != null) {

                LocalDateTime end = toEndOfDay(request.endDate());

                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), end));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static LocalDateTime toStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    private static LocalDateTime toEndOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }
}