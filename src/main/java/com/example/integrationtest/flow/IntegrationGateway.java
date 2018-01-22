package com.example.integrationtest.flow;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface IntegrationGateway {
    @Gateway(requestChannel = "channel")
    void execute(String message);
}
