package com.example.integrationtest.flow;

import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class MessageHandlerFactory {
    public AbstractMessageHandler createMessageHandler(Class<? extends AbstractMessageHandler> handlerClass){
        if(handlerClass.getName().equals(ConsoleMessagehandler.class.getName())){
            return new ConsoleMessagehandler();
        }
        /*else if(handlerClass.getClass().getName().equals(JdbcGenericMessageHandler.class.getName())){
            return new JdbcGenericMessageHandler();
        }*/
        return null;
    }
}
