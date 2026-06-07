package com.acharya.dikshanta.EcomMed.listeners;

import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;
import com.acharya.dikshanta.EcomMed.service.InventoryService;
import com.acharya.dikshanta.EcomMed.service.InvoiceService;
import com.acharya.dikshanta.EcomMed.service.impl.OrderPlacedServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class OrderPlacedEventListener {
    private final InventoryService inventoryService;
    private final OrderPlacedServiceImpl orderPlacedServiceImpl;
    private final InvoiceService invoiceService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    @TransactionalEventListener
    public void handleOrderPLacedEvent(OrderPlacedEvent event) {
        kafkaTemplate.send("order-topic",
                event.orderId().toString(),
                event);
    }


}

