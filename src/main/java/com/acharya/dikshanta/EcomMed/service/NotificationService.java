package com.acharya.dikshanta.EcomMed.service;


import com.acharya.dikshanta.EcomMed.events.UserRegisteredEvent;

public interface NotificationService {
    void sendSuccessfulRegistrationEmail(UserRegisteredEvent event);

}