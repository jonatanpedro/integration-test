package com.example.integrationtest.service;

import org.springframework.messaging.Message;

public class AuditService {

    public void update(Message<?> message){
        System.out.println(message.getPayload());
        //return message;
    }
}
