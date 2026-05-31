package com.acharya.dikshanta.EcomMed.model;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
