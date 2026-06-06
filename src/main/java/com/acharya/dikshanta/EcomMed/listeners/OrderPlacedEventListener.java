package com.acharya.dikshanta.EcomMed.listeners;

import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;
import com.acharya.dikshanta.EcomMed.service.InventoryService;
import com.acharya.dikshanta.EcomMed.service.InvoiceService;
import com.acharya.dikshanta.EcomMed.service.impl.OrderPlacedServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Component
public class OrderPlacedEventListener {
    private final InventoryService inventoryService;
    private final OrderPlacedServiceImpl orderPlacedServiceImpl;
    private final InvoiceService invoiceService;
    ExecutorService orderExecutor = Executors.newFixedThreadPool(10);

    @TransactionalEventListener
    public void handleOrderPLacedEvent(OrderPlacedEvent event) {
        CompletableFuture<Void> inventory =
                CompletableFuture.runAsync(
                        () -> inventoryService.decreaseInventoryStock(
                                event.orderItemEvents()),
                        orderExecutor);

        CompletableFuture<Void> email =
                CompletableFuture.runAsync(
                        () -> orderPlacedServiceImpl.sendEmail(event),
                        orderExecutor);

        CompletableFuture<Void> invoice =
                CompletableFuture.runAsync(
                        () -> invoiceService.generateInvoice(event),
                        orderExecutor);

        CompletableFuture.allOf(
                inventory,
                email,
                invoice
        ).join();
    }
}
