package com.example.integrationtest.service;

import com.example.integrationtest.dto.Flow;
import com.example.integrationtest.flow.*;
import com.example.integrationtest.repository.FlowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.jdbc.JdbcMessageHandler;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

@Service
public class FlowService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FlowRepository repository;

    @Autowired
    private TransformerFactory transformerFactory;

    @Autowired
    private MessageHandlerFactory messageHandlerFactory;

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IntegrationGateway gateway;

    public void createFlow(Flow flow){
        try {
            repository.save(flow);

            boolean hasDataChanged = flow.getSourceMap()
                    .entrySet()
                    .stream()
                    .anyMatch(entry -> entry.getValue().getDestination() != null);

            IntegrationFlowBuilder intFlowBuilder = IntegrationFlows.from("channel");

            intFlowBuilder = intFlowBuilder
                    .handle(Http.outboundGateway(flow.getUrlBase())
                            .charset("UTF-8")
                            .httpMethod(HttpMethod.GET)
                            .expectedResponseType(String.class));

            intFlowBuilder = intFlowBuilder.transform(new JsonToObjectTransformer());

            if(hasDataChanged){
                intFlowBuilder = intFlowBuilder.transform(transformerFactory.createTransformer(RestMapperTransformationService.class));
            }

            if(flow.getUseTable()){
                intFlowBuilder = intFlowBuilder.handle(new JdbcGenericMessageHandler(flow, dataSource));
            }

            if(flow.getUseConsole()){
                intFlowBuilder = intFlowBuilder.handle(messageHandlerFactory.createMessageHandler(ConsoleMessagehandler.class));
            }

            applicationContext.getAutowireCapableBeanFactory().autowireBean(intFlowBuilder.get());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public String executeFlow(String id){

        Flow flow = repository.findById(id).orElse(null);

        gateway.execute(flow.getId());

        return null;
    }
}
