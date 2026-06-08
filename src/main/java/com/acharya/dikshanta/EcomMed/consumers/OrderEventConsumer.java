package com.acharya.dikshanta.EcomMed.consumers;

import com.acharya.dikshanta.EcomMed.constrants.KafkaTopics;
import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;
import com.acharya.dikshanta.EcomMed.service.InventoryService;
import com.acharya.dikshanta.EcomMed.service.InvoiceService;
import com.acharya.dikshanta.EcomMed.service.impl.OrderPlacedServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderEventConsumer {
    private final InventoryService inventoryService;
    private final OrderPlacedServiceImpl orderPlacedService;
    private final InvoiceService invoiceService;


    @KafkaListener(groupId = "inventory-group", topics = KafkaTopics.ORDER_PLACED)
    public void decreaseInventory(@Payload OrderPlacedEvent event) {
        log.debug("decreaseInventory received event {}", event.orderId());
        try {
            inventoryService.decreaseInventoryStock(event.orderItemEvents());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(groupId = "notification-group", topics = KafkaTopics.ORDER_PLACED)
    public void sendNotification(@Payload OrderPlacedEvent event) {
        log.debug("sendNotification received event {}", event.orderId());
        try {
            orderPlacedService.sendEmail(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(groupId = "invoice-group", topics = KafkaTopics.ORDER_PLACED)
    public void generateReceipt(@Payload OrderPlacedEvent event) {
        log.debug("generateReceipt received event {}", event.orderId());
        try {
            invoiceService.generateInvoice(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
