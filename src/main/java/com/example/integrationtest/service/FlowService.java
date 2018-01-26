package com.example.integrationtest.service;

import com.example.integrationtest.dto.Flow;
import com.example.integrationtest.enums.ServiceType;
import com.example.integrationtest.flow.*;
import com.example.integrationtest.repository.FlowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.http.dsl.Http;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private IntegrationFlowContext context;

    @Autowired
    private IntegrationGateway gateway;

    public void createFlow(Flow flow){
        try {
            repository.save(flow);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public String executeFlow(String id){

        Flow flow = repository.findById(id).orElse(null);

        IntegrationFlowContext.IntegrationFlowRegistrationBuilder registration = context.registration(createIntegrationFlow(flow));

        gateway.execute(flow.getId());
        Date date = new Date();
        return String.format("Executado em %ta %tb %td %tT %tY", date, date, date, date, date);
    }

    public IntegrationFlow createIntegrationFlow(Flow flow){

        List<IntegrationFlow> flows = new ArrayList<>();

        boolean hasDataChanged = flow.getSourceMap()
                .entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue().getDestination() != null);

        IntegrationFlowBuilder intFlowBuilder = IntegrationFlows
                .from("channel")
                .enrich(enricherSpec -> enricherSpec.header("flow", flow));

        if (ServiceType.REST.equals(flow.getServiceType())) {
            intFlowBuilder = intFlowBuilder
                    .handle(Http.outboundGateway(flow.getUrlBase())
                            .charset("UTF-8")
                            .httpMethod(HttpMethod.GET)
                            .expectedResponseType(String.class));
        }

        if(hasDataChanged){
            intFlowBuilder = intFlowBuilder.transform(transformerFactory.createTransformer(RestMapperTransformationService.class));
        }

        if(flow.getUseConsole()){
            flows.add(f -> f.handle(messageHandlerFactory.createMessageHandler(ConsoleMessagehandler.class)));
        }

        if(flow.getUseTable()){
            flows.add(f -> f.handle(new JdbcGenericMessageHandler(flow, dataSource)));
        }

        intFlowBuilder = intFlowBuilder.publishSubscribeChannel(command -> flows.forEach(command::subscribe));

        return intFlowBuilder.get();
    }
}
