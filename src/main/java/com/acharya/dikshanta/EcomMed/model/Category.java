package com.acharya.dikshanta.EcomMed.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(name = "unique_category", columnNames = {"name"})
})

public class Category extends BaseEntity {
    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 500, nullable = false)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    //Private helper to add product
    void addProduct(Product product) {
        this.products.add(product);
        product.setCategory(this);
    }

    //Private helper to remove product
    void removeProduct(Product product) {
        this.products.remove(product);
        product.setCategory(null);
    }
}
