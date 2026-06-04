package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;

public interface OrderNotificationService {
    public void sendEmail(OrderPlacedEvent orderPlacedEvent);


}
