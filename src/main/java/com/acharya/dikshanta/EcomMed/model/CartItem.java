package com.acharya.dikshanta.EcomMed.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private Integer quantity;

    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public void calculateTotalPrice() {
        totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
