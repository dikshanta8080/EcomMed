package com.acharya.dikshanta.EcomMed.service;


import com.acharya.dikshanta.EcomMed.events.UserRegisteredEvent;

public interface RegistrationNotificationService {
    void sendSuccessfulRegistrationEmail(UserRegisteredEvent event);



}