package com.acharya.dikshanta.EcomMed.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Invoice extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private BigDecimal totalPrice;
}
