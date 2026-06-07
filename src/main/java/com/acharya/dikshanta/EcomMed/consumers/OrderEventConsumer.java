package com.acharya.dikshanta.EcomMed.consumers;

import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;
import com.acharya.dikshanta.EcomMed.service.InventoryService;
import com.acharya.dikshanta.EcomMed.service.InvoiceService;
import com.acharya.dikshanta.EcomMed.service.impl.OrderPlacedServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {
    private final InventoryService inventoryService;
    private final OrderPlacedServiceImpl orderPlacedService;
    private final InvoiceService invoiceService;


    @KafkaListener(groupId = "inventory-group", topics = "order-topic")
    public void decreaseInventory(OrderPlacedEvent event) {
        log.debug("decreaseInventory received event {}", event.orderId());
        inventoryService.decreaseInventoryStock(event.orderItemEvents());
    }

    @KafkaListener(groupId = "notification-group", topics = "order-topic")
    public void sendNotification(OrderPlacedEvent event) {
        log.debug("sendNotification received event {}", event.orderId());
        orderPlacedService.sendEmail(event);
    }

    @KafkaListener(groupId = "invoice-group", topics = "order-topic")
    public void generateReceipt(OrderPlacedEvent event) {
        log.debug("generateReceipt received event {}", event.orderId());
        invoiceService.generateInvoice(event);
    }


}
