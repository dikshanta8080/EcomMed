package com.acharya.dikshanta.EcomMed.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "inventories")
public class Inventory extends BaseEntity {
    private String name;
    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
