package com.example.integrationtest.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;

public class ConsoleMessagehandler extends AbstractMessageHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void handleMessageInternal(Message<?> message) {
        log.info(message.getPayload().toString());
    }
}
