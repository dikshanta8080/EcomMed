package com.acharya.dikshanta.EcomMed.consumers;

import com.acharya.dikshanta.EcomMed.service.InventoryService;
import com.acharya.dikshanta.EcomMed.service.InvoiceService;
import com.acharya.dikshanta.EcomMed.service.impl.OrderPlacedServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor

@KafkaListener(topics = "order-placed")
public class OrderEventConsumer {
    private final InventoryService inventoryService;
    private final OrderPlacedServiceImpl orderPlacedService;
    private final InvoiceService invoiceService;

    public void consume() {

    }
}
