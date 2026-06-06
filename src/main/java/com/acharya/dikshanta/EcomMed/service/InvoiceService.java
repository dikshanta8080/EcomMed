package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;
import com.acharya.dikshanta.EcomMed.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.EcomMed.model.Invoice;
import com.acharya.dikshanta.EcomMed.model.Order;
import com.acharya.dikshanta.EcomMed.repository.InvoiceRepository;
import com.acharya.dikshanta.EcomMed.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    public void generateInvoice(OrderPlacedEvent event) {
        if (!invoiceRepository.existsByOrderId(event.orderId())) {
            Order order = orderRepository.findById(event.orderId()).orElseThrow(() ->
                    new ResourceNotFoundException("Order not found"));
            Invoice invoice = Invoice.builder().order(order).totalPrice(order.getTotalPrice())
                    .build();
            invoiceRepository.save(invoice);
        }
    }
}
